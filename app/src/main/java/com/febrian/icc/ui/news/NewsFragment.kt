package com.febrian.icc.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.febrian.icc.R
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ItemNewsBinding
import com.febrian.icc.databinding.NewsFragmentBinding
import com.febrian.icc.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class NewsFragment : Fragment(), EventNews {

    private var _binding: NewsFragmentBinding? = null
    private val binding get() = _binding!!

    private val listNews: ArrayList<NewsDataResponse> = ArrayList()

    private lateinit var viewModel: NewsViewModel

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(listNews, requireActivity(), this)
        viewModel = ViewModelFactory.getInstance(requireContext()).create(NewsViewModel::class.java)
        loading(viewModel)
        observerNews(viewModel, "covid")
        searchNews(viewModel)

        binding.btnBookmarks.setOnClickListener {
            val intent = Intent(requireContext(), BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchNews(viewModel: NewsViewModel) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                observerNews(viewModel, query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                observerNews(viewModel, newText.toString())
                return true
            }
        })
    }

    private fun observerNews(viewModel: NewsViewModel, query: String) {

        viewModel.getNews(query).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    showNews(it.data.articles)
                }
                is ApiResponse.Error -> {
                    showMessage(it.errorMessage)
                }
                else -> {}
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showNews(data: ArrayList<NewsDataResponse>?) {
        if (data != null) {
            for (i in 0 until data.size) {
                val newsData = NewsDataResponse(
                    data[i].title?.split(" - ")?.get(0).toString(),
                    data[i].url,
                    data[i].urlToImage,
                    data[i].publishedAt?.split("T")?.get(0).toString()
                )
                listNews.add(newsData)
            }
            showRecycleView()
        }
    }

    private fun showRecycleView() {
        val adapterHeading = NewsAdapterHorizontal(listNews)
        binding.rvNewsHeading.setHasFixedSize(true)
        binding.rvNewsHeading.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvNewsHeading.adapter = adapterHeading

        binding.rvNews.setHasFixedSize(true)
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter
    }

    private fun loading(viewModel: NewsViewModel) {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.shimmerFrameLayout.startShimmer()
                binding.shimmerFrameLayout.visibility = View.VISIBLE
                binding.rvNews.visibility = View.GONE

                binding.shimmerFrameLayout1.startShimmer()
                binding.shimmerFrameLayout1.visibility = View.VISIBLE
                binding.rvNewsHeading.visibility = View.GONE
            } else {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE

                binding.shimmerFrameLayout1.stopShimmer()
                binding.shimmerFrameLayout1.visibility = View.GONE
                binding.rvNewsHeading.visibility = View.VISIBLE
            }

            binding.refreshLayout.isRefreshing = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBookmark(news: NewsDataResponse, view: View, binding: Any) {
        val bind = binding as ItemNewsBinding
        viewModel.newsExist(news.title.toString()).observe(this) {
            if (!it) {
                val entityNews = EntityNews(
                    title = news.title.toString(),
                    url = news.url.toString(),
                    urlToImage = news.urlToImage.toString(),
                    publishedAt = news.publishedAt.toString()
                )
                Snackbar.make(
                    view,
                    "News success add to bookmark",
                    Snackbar.LENGTH_SHORT
                ).show()
                Glide.with(view.context)
                    .load(R.drawable.ic_baseline_bookmark_24)
                    .into(bind.bookmark)

                viewModel.insert(entityNews)
            } else {
                Snackbar.make(
                    view,
                    "News success delete from bookmark",
                    Snackbar.LENGTH_SHORT
                ).show()
                Glide.with(view.context).load(R.drawable.ic_baseline_bookmark_border_24)
                    .into(binding.bookmark)
                viewModel.delete(news.title.toString())
            }
        }
    }

    override fun onShare(url: String) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(mimeType)
            .setChooserTitle("Share")
            .setText(url)
            .startChooser()
    }

}