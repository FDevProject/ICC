package com.febrian.icc.ui.global

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.febrian.icc.MapsActivity
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.databinding.FragmentGlobalBinding
import com.febrian.icc.utils.ConnectionReceiver
import com.febrian.icc.utils.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class GlobalFragment : Fragment(), OnMapReadyCallback, ConnectionReceiver.ReceiveListener {

    private var _binding: FragmentGlobalBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: GlobalViewModel
    private lateinit var receiver: ConnectionReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            viewModel =
                ViewModelFactory.getInstance(requireContext()).create(GlobalViewModel::class.java)
            receiver = ConnectionReceiver(requireContext(), this)

            main()
            binding.refreshLayout.setOnRefreshListener {
                main()
            }

            binding.btnMaps.setOnClickListener {
                val intent = Intent(requireContext(), MapsActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun main() {
        loading()
        observerData()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapGlobal) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun observerData() {
        viewModel.getGlobal().observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    showUIData(it.data)
                    showDataPieChart(it.data)
                }
                is ApiResponse.Error -> showMessage(it.errorMessage)
                else -> {}
            }
        }
    }

    private fun loading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) binding.loading.visibility = View.VISIBLE
            else binding.loading.visibility = View.GONE

            binding.refreshLayout.isRefreshing = it
        }

    }

    private fun showUIData(data: CovidResponse) {
        binding.confirmed.text = data.active.toString()
        binding.recovered.text = data.recovered.toString()
        binding.deceased.text = data.deaths.toString()

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tgl.text = currentDate
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDataPieChart(data: CovidResponse) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()

        listPie.add(PieEntry(data.active.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.yellow_primary))
        listPie.add(PieEntry(data.recovered.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.green_custom))
        listPie.add(PieEntry(data.deaths.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.red_custom))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //set text center
        binding.pieChart.centerText = "Total Case\n${NumberFormat.getInstance().format(data.cases)}"
        binding.pieChart.setCenterTextColor(Color.argb(255, 80, 125, 188))
        binding.pieChart.setCenterTextSize(18f)
        binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        binding.pieChart.legend.isEnabled = false // hide tags labels

        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setTransparentCircleColor(android.R.color.white)

        binding.pieChart.holeRadius = 75f

        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false

    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val location = getCountry()
        val latLng: LatLng = getLatLng(location)

        mMap.addMarker(MarkerOptions().position(latLng).title(location))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                ), 3f
            )
        )
    }

    private fun getCountry(): String {
        var location = resources.configuration?.locale?.displayCountry

        if (location == "" || location == null)
            location = "Indonesia"

        binding.country.text = location

        return location
    }

    private fun getLatLng(country: String): LatLng {
        var latLng = LatLng(0.0, 0.0)
        val geocoder = Geocoder(requireContext())
        try {
            val addressList = geocoder.getFromLocationName(country, 1)
            val address = addressList!![0]
            latLng = LatLng(address!!.latitude, address.longitude)
        } catch (e: IOException) {
            showMessage(e.message.toString())
            e.printStackTrace()
        }

        return latLng
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(receiver, intent)
        super.onStart()
    }

    override fun onStop() {
        requireContext().unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onNetworkChange() {
        main()
    }

}