package com.febrian.icc.data.source.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    val apiCovid : ApiService by lazy {
        val service = Retrofit
            .Builder()
            .baseUrl("https://disease.sh")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service.create(ApiService::class.java)
    }

    val apiCovidProvince : ApiService by lazy {
        val service = Retrofit
            .Builder()
            .baseUrl("https://covid19.mathdro.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service.create(ApiService::class.java)
    }

    val news : ApiService by lazy {
        val service = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service.create(ApiService::class.java)
    }
}