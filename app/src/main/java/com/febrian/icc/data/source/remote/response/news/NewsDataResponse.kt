package com.febrian.icc.data.source.remote.response.news

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsDataResponse(
    val title : String?,
    val url:String?,
    val urlToImage:String?,
    val publishedAt:String?
) : Parcelable
