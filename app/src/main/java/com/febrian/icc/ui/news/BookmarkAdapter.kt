package com.febrian.icc.ui.news

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.databinding.ItemBookmarkBinding
import com.febrian.icc.utils.Constant


class BookmarkAdapter(
    private val listNews: ArrayList<EntityNews>,
    private val activity: Activity,
    private val listener: EventNews
) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: EntityNews) {

            binding.titleNews.text = news.title.toString()
            binding.tglNews.text = news.publishedAt.toString()
            Glide.with(itemView.context).load(news.urlToImage).into(binding.imageNews)

            binding.titleNews.setOnClickListener {
                val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                intent.putExtra(Constant.KEY_NEWS, news)
                itemView.context.startActivity(intent)
            }

            binding.bookmarkInBookmark.setOnClickListener {
                val newsResponse = NewsDataResponse(
                    title = news.title.toString(),
                    url = news.url.toString(),
                    urlToImage = news.urlToImage.toString(),
                    publishedAt = news.publishedAt.toString()
                )
                listener.onBookmark(newsResponse, itemView, binding)
                listNews.removeAt(adapterPosition)
                notifyDataSetChanged()
            }

            binding.share.setOnClickListener {
                listener.onShare(news.url.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.ViewHolder {
        val binding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkAdapter.ViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int {
        return listNews.size
    }
}