package com.febrian.icc.ui.news

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ItemNewsBinding
import com.febrian.icc.utils.Constant.KEY_NEWS

class NewsAdapter(
    private val listNews: ArrayList<NewsDataResponse>,
    private val activity: Activity,
    private val listener: EventNews
) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsDataResponse) {
            binding.titleNews.text = news.title.toString()
            binding.dateNews.text = news.publishedAt.toString()
            Glide.with(itemView.context)
                .load(news.urlToImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                )
                .into(binding.imageNews)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                intent.putExtra(KEY_NEWS, news)
                activity.startActivity(intent)
            }

            binding.bookmark.setOnClickListener {
                listener.onBookmark(news, itemView, binding)
            }

            binding.share.setOnClickListener {
                listener.onShare(news.url.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int {
        return if (listNews.size > 10) 10
        else listNews.size
    }
}