package com.febrian.icc.ui.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.ProvinceResponse

class GlobalViewModel(private val repository: Repository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getGlobal(): LiveData<ApiResponse<CovidResponse>> {
        return repository.getGlobal(_isLoading)
    }

    fun getProvince(): LiveData<ApiResponse<ArrayList<ProvinceResponse>>> {
        return repository.getProvince(_isLoading)
    }
}