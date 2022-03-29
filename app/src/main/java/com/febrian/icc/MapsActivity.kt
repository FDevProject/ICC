package com.febrian.icc

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.databinding.ActivityMapsBinding
import com.febrian.icc.ui.global.GlobalViewModel
import com.febrian.icc.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var viewModel: GlobalViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelFactory.getInstance(this).create(GlobalViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapGlobal) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = query.toString()

                viewModel!!.getProvince().observe(this@MapsActivity) {
                    when (it) {
                        is ApiResponse.Success -> {

                            it.data.forEach { data ->
                                if (data.long != null || data.lat != null || data.combinedKey != null) {

                                    if (data.combinedKey?.uppercase(Locale.getDefault())!!.contains(
                                            location.uppercase(
                                                Locale.getDefault()
                                            )
                                        )
                                    ) {
                                        mMap.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    data.lat!!,
                                                    data.long!!
                                                ), 9f
                                            )
                                        )
                                    }
                                }
                            }

                        }
                        is ApiResponse.Error -> {
                            showMessage(it.errorMessage)
                        }
                        else -> {}
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun isDarkModeOn(): Boolean {

        return when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = if (isDarkModeOn()) {
                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_maps
                    )
                )
            } else {
                googleMap.setMapStyle(null)
            }

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }

        viewModel!!.getProvince().observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    it.data.forEach { data ->

                        if (data.lat != null && data.long != null) {
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        data.lat,
                                        data.long
                                    )
                                ).title(data.combinedKey.toString())
                            )
                            mMap.setOnMarkerClickListener { v ->
                                val bottomSheet = BottomSheetDialog(
                                    this@MapsActivity,
                                    R.style.BottomSheetDialogTheme
                                )
                                val bottomSheetView: View =
                                    LayoutInflater.from(applicationContext)
                                        .inflate(
                                            R.layout.item_bottom_sheet,
                                            findViewById<LinearLayout>(R.id.bottomSheetContainer)
                                        )
                                bottomSheetView.findViewById<TextView>(R.id.text_country).text =
                                    v.title.toString()

                                if (v.title == data.combinedKey) {
                                    val stamp = Timestamp(data.lastUpdate!!)
                                    val date =
                                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                                            Date(stamp.time)
                                        )

                                    val lastUpdate =
                                        applicationContext.resources.getString(R.string.last_update)

                                    bottomSheetView.findViewById<TextView>(R.id.last_update).text =
                                        "$lastUpdate $date"

                                    if(data.active == null)
                                        data.active = 0
                                    if (data.confirmed == null)
                                        data.confirmed = 0
                                    if (data.deaths == null)
                                        data.deaths = 0
                                    if(data.recovered == null)
                                        data.recovered = 0

                                    bottomSheetView.findViewById<TextView>(R.id.text_confirmed).text =
                                        data.active.toString()
                                    bottomSheetView.findViewById<TextView>(R.id.text_deceased).text =
                                        data.deaths.toString()
                                    bottomSheetView.findViewById<TextView>(R.id.text_recovered).text =
                                        data.recovered.toString()
                                    bottomSheetView.findViewById<TextView>(R.id.text_total_case).text =
                                        data.confirmed.toString()
                                }

                                bottomSheet.setContentView(bottomSheetView)
                                bottomSheet.show()
                                true
                            }
                        }
                    }
                }
                is ApiResponse.Error -> {
                    showMessage(it.errorMessage)
                }
                else -> {}
            }
        }
    }
}