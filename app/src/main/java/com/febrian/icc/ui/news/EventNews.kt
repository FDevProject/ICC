package com.febrian.icc.ui.news

import android.view.View
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse

interface EventNews {
    fun onBookmark(news: NewsDataResponse, view: View, binding: Any)
    fun onShare(url: String)
}