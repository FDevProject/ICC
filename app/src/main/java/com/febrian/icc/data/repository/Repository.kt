package com.febrian.icc.data.repository

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.febrian.icc.data.source.DataSource
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.local.LocalDataSource
import com.febrian.icc.data.source.remote.RemoteDataSource
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.ProvinceResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse
import com.febrian.icc.data.source.remote.response.info.DropdownInfoResponse
import com.febrian.icc.data.source.remote.response.info.ListInfoResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DataSource {

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            localDataSource: LocalDataSource
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(remoteData, localDataSource).apply { instance = this }
            }
    }

    override fun getCountries(
        country: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<CovidResponse>> {
        return remoteDataSource.getCountries(country, isLoading)
    }

    override fun getStatisticCountries(
        country: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<StatisticResponse>> {
        return remoteDataSource.getStatistic(country)
    }

    override fun getGlobal(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<CovidResponse>> {
        return remoteDataSource.getGlobal(isLoading)
    }

    override fun getProvince(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<ArrayList<ProvinceResponse>>> {
        return remoteDataSource.getProvince(isLoading)
    }

    override fun getNews(
        query: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<NewsResponse>> {
        return remoteDataSource.getNews(query, isLoading)
    }

    override fun getFavoriteNews(
        query: String
    ): LiveData<EntityNews> {
        val result = MutableLiveData<EntityNews>()
        result.value = localDataSource.getNewsByTitle(query)
        return result
    }

    override fun insert(news: EntityNews) {
        localDataSource.insert(news)
    }

    override fun delete(title: String) {
        localDataSource.delete(title)
    }

    override fun getAllNews(): LiveData<List<EntityNews>> {
        val result = MutableLiveData<List<EntityNews>>()
        result.value = localDataSource.getAllNews()
        return result
    }

    override fun newsExist(title: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = localDataSource.newsExist(title)
        return result
    }

    override fun newsExistState(title: String, state: MutableState<Boolean?>): MutableState<Boolean?> {
        state.value = localDataSource.newsExist(title)
        return state
    }

    override fun getListInfo(
        title: String,
        loading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<ListInfoResponse>> {
        return remoteDataSource.getListInfo(title, loading)
    }

    override fun getDropdownInfo(
        title: String,
        loading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<DropdownInfoResponse>> {
        return remoteDataSource.getDropdownInfo(title, loading)
    }
}