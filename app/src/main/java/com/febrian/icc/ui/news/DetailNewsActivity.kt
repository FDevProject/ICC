package com.febrian.icc.ui.news

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.febrian.icc.R
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ActivityDetailNewsBinding
import com.febrian.icc.utils.ConnectionReceiver
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailNewsActivity : AppCompatActivity(), ConnectionReceiver.ReceiveListener {

    private lateinit var binding: ActivityDetailNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var receiver : ConnectionReceiver

    private var news: NewsDataResponse? = null

    @SuppressLint("UseCompatLoadingForDrawables", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = ConnectionReceiver(this, this)
        news = intent.getParcelableExtra(Constant.KEY_NEWS)

        viewModel =
            ViewModelFactory.getInstance(applicationContext).create(NewsViewModel::class.java)

        newsExist(viewModel, news, news?.title.toString())

        binding.loading.visibility = View.VISIBLE

        binding.back.setOnClickListener {
            finish()
        }

        val action = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_open_anim)
        val items = AnimationUtils.loadAnimation(applicationContext, R.anim.from_bottom_anim)

        val actionClose = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_close_anim)
        val itemsClose = AnimationUtils.loadAnimation(applicationContext, R.anim.to_bottom_anim)

        var show = false

        binding.action.setOnClickListener {
            show = !show

            if (show) {
                binding.bookmark.visibility = View.VISIBLE
                binding.share.visibility = View.VISIBLE
                binding.action.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_clear_24
                    )
                )
                binding.bookmark.startAnimation(items)
                binding.share.startAnimation(items)
                binding.action.startAnimation(action)
            } else {
                binding.bookmark.visibility = View.INVISIBLE
                binding.share.visibility = View.INVISIBLE
                binding.action.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_add_24
                    )
                )
                binding.bookmark.startAnimation(itemsClose)
                binding.share.startAnimation(itemsClose)
                binding.action.startAnimation(actionClose)
            }
        }

        binding.share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(this@DetailNewsActivity)
                .setType(mimeType)
                .setChooserTitle("Share")
                .setText(news?.url)
                .startChooser()
        }

        main()
    }

    private fun insert(view: View, news: EntityNews, viewModel: NewsViewModel) {
        Snackbar.make(view, "News success add to bookmark", Snackbar.LENGTH_SHORT).show()
        Glide.with(applicationContext)
            .load(R.drawable.ic_baseline_bookmark_24)
            .into(binding.bookmark)
        viewModel.insert(news)
    }

    private fun delete(view: View, viewModel: NewsViewModel, title: String) {
        Snackbar.make(view, "News success delete from bookmark", Snackbar.LENGTH_SHORT).show()
        Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_border_24)
            .into(binding.bookmark)
        viewModel.delete(title)
    }

    private fun newsExist(viewModel: NewsViewModel, news: NewsDataResponse?, title: String) {
        viewModel.newsExist(title).observe(this) { data ->
            if (data) {
                Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_24)
                    .into(binding.bookmark)
            } else {
                Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_border_24)
                    .into(binding.bookmark)
            }
        }

        binding.bookmark.setOnClickListener {
            viewModel.newsExist(title).observe(this) { data ->
                val entityNews = EntityNews(
                    title = news?.title.toString(),
                    url = news?.url.toString(),
                    urlToImage = news?.urlToImage.toString(),
                    publishedAt = news?.publishedAt.toString()
                )

                if (!data) insert(it, entityNews, viewModel)
                else delete(it, viewModel, news?.title.toString())
            }
        }

    }

    private fun main() {
        binding.web.settings.javaScriptEnabled = true
        binding.web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //   view?.loadUrl("javascript:alert('Web Berhasil Di Load')")
                //   binding.refreshLayout.visibility = View.GONE

                binding.loading.visibility = View.GONE
            }
        }

        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                result?.confirm()
                return true
            }
        }
        binding.web.loadUrl(news?.url.toString())

    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, intent)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onNetworkChange() {
        main()
    }
}