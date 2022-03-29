package com.febrian.icc.ui.news

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ActivityBookmarkBinding
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class BookmarkActivity : AppCompatActivity(), EventNews {

    private lateinit var binding: ActivityBookmarkBinding

    private var viewModel: NewsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelFactory.getInstance(applicationContext).create(NewsViewModel::class.java)

        viewModel?.getAllNews()?.observe(this) {
            val adapter =
                BookmarkAdapter(it as ArrayList<EntityNews>, activity = this@BookmarkActivity, this)
            adapter.notifyDataSetChanged()
            binding.rvBookmark.setHasFixedSize(true)
            binding.rvBookmark.layoutManager = LinearLayoutManager(this)
            binding.rvBookmark.adapter = adapter

            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.GONE
            binding.rvBookmark.visibility = View.VISIBLE
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onBookmark(news: NewsDataResponse, view: View, binding: Any) {
        val sharedPref =
            view.context.getSharedPreferences(Constant.PREFERENCE, Context.MODE_PRIVATE)
        sharedPref.edit().remove(news.title).apply()

        Snackbar.make(view, "News success delete from bookmark", Snackbar.LENGTH_SHORT).show()
        viewModel?.delete(news.title.toString())
    }

    override fun onShare(url: String) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(this)
            .setType(mimeType)
            .setChooserTitle("Share")
            .setText(url)
            .startChooser()
    }
}