package com.febrian.icc.data.source.remote.network

import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.ProvinceResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/v3/covid-19/countries/{country}")
    fun getCountries(
        @Path("country") country: String
    ) : Call<CovidResponse>

    @GET("/v3/covid-19/all")
    fun getGlobal() : Call<CovidResponse>

    @GET("api/confirmed")
    fun getProvince() : Call<ArrayList<ProvinceResponse>>

    @GET("/v2/top-headlines")
    fun getNews(
        @Query("q") q : String,
        @Query("category") category : String,
        @Query("apiKey") apiKey : String
    ) : Call<NewsResponse>
}