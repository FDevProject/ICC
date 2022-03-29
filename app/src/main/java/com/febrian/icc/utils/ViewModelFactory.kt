package com.febrian.icc.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.febrian.icc.data.repository.Repository
import com.febrian.icc.di.Injection
import com.febrian.icc.ui.global.GlobalViewModel
import com.febrian.icc.ui.home.HomeViewModel
import com.febrian.icc.ui.info.InfoViewModel
import com.febrian.icc.ui.news.NewsViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(GlobalViewModel::class.java) -> {
                GlobalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(InfoViewModel::class.java) -> {
                InfoViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}