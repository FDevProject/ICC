package com.febrian.icc.ui.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ItemHeadingNewsBinding
import com.febrian.icc.utils.Constant

class NewsAdapterHorizontal(private val listNews: ArrayList<NewsDataResponse>) :
    RecyclerView.Adapter<NewsAdapterHorizontal.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemHeadingNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsDataResponse) {

            binding.titleNews.text = news.title
            Glide.with(itemView.context)
                .load(news.urlToImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                )
                .into(binding.imageNews)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                intent.putExtra(Constant.KEY_NEWS, news)
                itemView.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemHeadingNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int {
        return if (listNews.size > 3) 3
        else listNews.size
    }
}