package com.febrian.icc.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.info.DropdownInfoResponse
import com.febrian.icc.data.source.remote.response.info.ListInfoResponse

class InfoViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDropdownInfo(title: String): LiveData<ApiResponse<DropdownInfoResponse>> {
        return repository.getDropdownInfo(title, _isLoading)
    }

    fun getListInfo(title: String): LiveData<ApiResponse<ListInfoResponse>> {
        return repository.getListInfo(title, _isLoading)
    }
}