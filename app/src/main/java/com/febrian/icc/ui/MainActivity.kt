package com.febrian.icc.ui

import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.febrian.icc.R
import com.febrian.icc.ui.home.compose.BottomNavigationBar
import com.febrian.icc.ui.home.compose.NavigationSetup
import com.febrian.icc.ui.ui.theme.ICCTheme
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class MainActivity : ComponentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val navController = rememberNavController()
                    Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
                        NavigationSetup(
                            navController = navController,
                            context = applicationContext,
                            this
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
     /*  val location = getCountry()
        val latLng: LatLng = getLatLng(location)

        mMap.addMarker(MarkerOptions().position(latLng).title(location))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                ), 3f
            )
        )*/
    }

    private fun getCountry(resources: Resources, view: View): String {
        var location = resources.configuration?.locale?.displayCountry

        if (location == "" || location == null)
            location = "Indonesia"

        view.findViewById<TextView>(R.id.country).text = location

        return location
    }

    private fun getLatLng(country: String, context: Context): LatLng {
        var latLng = LatLng(0.0, 0.0)
        val geocoder = Geocoder(context)
        try {
            val addressList = geocoder.getFromLocationName(country, 1)
            val address = addressList!![0]
            latLng = LatLng(address!!.latitude, address.longitude)
        } catch (e: IOException) {
          //  showMessage(e.message.toString(), context)
            e.printStackTrace()
        }

        return latLng
    }
}
