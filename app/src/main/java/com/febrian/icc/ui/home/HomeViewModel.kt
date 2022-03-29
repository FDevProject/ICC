package com.febrian.icc.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCountry(country: String): LiveData<ApiResponse<CovidResponse>> {
        return repository.getCountries(country, _isLoading)
    }

    fun getStatistic(country: String) : LiveData<ApiResponse<StatisticResponse>>{
        return repository.getStatisticCountries(country, _isLoading)
    }

}