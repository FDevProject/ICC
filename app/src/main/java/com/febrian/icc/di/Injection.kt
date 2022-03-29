package com.febrian.icc.di

import android.content.Context
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.data.source.local.LocalDataSource
import com.febrian.icc.data.source.local.NewsDatabase
import com.febrian.icc.data.source.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context): Repository {

        val database = NewsDatabase.getDatabase(context)

        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.newsDao())
        return Repository.getInstance(remoteDataSource, localDataSource)
    }
}