package com.febrian.icc.data.source.remote.response.news

data class NewsResponse(
    val status:String?,
    val totalResults:String?,
    val articles : ArrayList<NewsDataResponse>?
)
