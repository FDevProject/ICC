package com.febrian.icc.ui.info

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.databinding.ActivityListInfoBinding
import com.febrian.icc.utils.ConnectionReceiver
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

class ListInfoActivity : AppCompatActivity(), ConnectionReceiver.ReceiveListener {

    private lateinit var binding: ActivityListInfoBinding
    private lateinit var viewModel: InfoViewModel
    private lateinit var receiver: ConnectionReceiver

    private var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getStringExtra(Constant.KEY_INFO)
        viewModel = ViewModelFactory.getInstance(this).create(InfoViewModel::class.java)
        receiver = ConnectionReceiver(this, this)

        binding.title.text = intent.getStringExtra(Constant.KEY_TITLE_INFO).toString()

        main()

        binding.refreshLayout.setOnRefreshListener {
            main()
        }
    }

    private fun main() {
        showData(data.toString())
        loading()
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showData(data: String) {
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

    private fun loading() {
        viewModel.isLoading.observe(this) {
            if (it)
                binding.loading.visibility = View.VISIBLE
            else
                binding.loading.visibility = View.GONE

            binding.refreshLayout.isRefreshing = it
        }
    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, intent)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onNetworkChange() {
        main()
    }
}