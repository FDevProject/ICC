package com.febrian.icc.ui.info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.databinding.ActivityListInfoBinding
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

class ListInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getStringExtra(Constant.KEY_INFO)
        val viewModel = ViewModelFactory.getInstance(this).create(InfoViewModel::class.java)

        binding.title.text = intent.getStringExtra(Constant.KEY_TITLE_INFO).toString()

        showData(viewModel, data.toString())
        loading(viewModel)
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showData(viewModel: InfoViewModel, data: String) {
        viewModel.getListInfo(data).observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    binding.rv.setHasFixedSize(true)
                    binding.rv.layoutManager = LinearLayoutManager(this)
                    binding.rv.adapter = ListInfoAdapter(it.data.data)

                    binding.back.setOnClickListener { finish() }
                }
                is ApiResponse.Error -> {
                    showMessage(it.errorMessage)
                }
                else -> {}
            }
        }
    }

    private fun loading(viewModel: InfoViewModel) {
        viewModel.isLoading.observe(this) {
            if (it)
                binding.loading.visibility = View.VISIBLE
            else
                binding.loading.visibility = View.GONE
        }
    }
}