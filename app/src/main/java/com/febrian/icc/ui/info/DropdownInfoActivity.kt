package com.febrian.icc.ui.info

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.databinding.ActivityDropdownInfoBinding
import com.febrian.icc.utils.ConnectionReceiver
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

class DropdownInfoActivity : AppCompatActivity(), ConnectionReceiver.ReceiveListener {

    private lateinit var binding: ActivityDropdownInfoBinding
    private lateinit var viewModel: InfoViewModel
    private lateinit var receiver: ConnectionReceiver

    private var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropdownInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = ConnectionReceiver(this, this)
        viewModel = ViewModelFactory.getInstance(this).create(InfoViewModel::class.java)

        binding.title.text = intent.getStringExtra(Constant.KEY_TITLE_INFO).toString()

        data = intent.getStringExtra(Constant.KEY_INFO)
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
        viewModel.getDropdownInfo(data).observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    binding.rv.setHasFixedSize(true)
                    binding.rv.layoutManager = LinearLayoutManager(this)
                    binding.rv.adapter = DropdownInfoAdapter(it.data.data)

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
        applicationContext.registerReceiver(receiver, intent)
        super.onStart()
    }

    override fun onStop() {
        applicationContext.unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onNetworkChange() {
        main()
    }
}