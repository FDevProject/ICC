package com.febrian.icc.ui.news

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse

class NewsViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getNews(query: String): LiveData<ApiResponse<NewsResponse>> {
        return repository.getNews(query, _isLoading)
    }

    fun getFavoriteNews(query: String): LiveData<EntityNews> {
        return repository.getFavoriteNews(query)
    }

    fun newsExist(title : String) : LiveData<Boolean>{
        return repository.newsExist(title)
    }

    fun newsExistState(title : String, state: MutableState<Boolean?>) : MutableState<Boolean?>{
        return repository.newsExistState(title, state)
    }

    fun insert(news : EntityNews){
        repository.insert(news)
    }

    fun delete(title : String){
        repository.delete(title)
    }

    fun getAllNews():LiveData<List<EntityNews>>{
        return repository.getAllNews()
    }
}