package com.febrian.icc.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.febrian.icc.ui.home.compose.BottomNavigationBar
import com.febrian.icc.ui.home.compose.NavigationSetup
import com.febrian.icc.ui.ui.theme.ICCTheme
import com.febrian.icc.utils.Constant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.Black
                ) {
                    val navController = rememberNavController()
                    Scaffold(bottomBar = { BottomNavigationBar(navController) }) {

                        val sharedPref = applicationContext.getSharedPreferences(Constant.KEY_LOG, Context.MODE_PRIVATE)
                        sharedPref.edit().putString(Constant.KEY_LOG, Constant.KEY_LOG).apply()

                        NavigationSetup(
                            navController = navController
                        )
                    }
                }
            }
        }
    }

}
