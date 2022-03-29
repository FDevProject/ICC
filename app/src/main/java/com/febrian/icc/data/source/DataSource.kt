package com.febrian.icc.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.ProvinceResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse
import com.febrian.icc.data.source.remote.response.info.DropdownInfoResponse
import com.febrian.icc.data.source.remote.response.info.ListInfoResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse

interface DataSource {

    fun getCountries(
        country: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<CovidResponse>>

    fun getStatisticCountries(
        country: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<StatisticResponse>>

    fun getGlobal(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<CovidResponse>>

    fun getNews(
        query: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<NewsResponse>>

    fun getProvince(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<ArrayList<ProvinceResponse>>>

    fun getFavoriteNews(
        query: String
    ): LiveData<EntityNews>

    fun insert(news : EntityNews)

    fun delete(title : String)

    fun getAllNews():LiveData<List<EntityNews>>

    fun newsExist(title: String) : LiveData<Boolean>

    fun getListInfo(title : String, loading : MutableLiveData<Boolean>) : LiveData<ApiResponse<ListInfoResponse>>

    fun getDropdownInfo(title : String, loading : MutableLiveData<Boolean>) : LiveData<ApiResponse<DropdownInfoResponse>>
}