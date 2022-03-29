package com.febrian.icc.data.source.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName ="news")
data class EntityNews(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title")
    val title : String?,
    @ColumnInfo(name = "url")
    val url:String?,
    @ColumnInfo(name = "urlToImage")
    val urlToImage:String?,
    @ColumnInfo(name = "publishedAt")
    val publishedAt:String?
) : Parcelable