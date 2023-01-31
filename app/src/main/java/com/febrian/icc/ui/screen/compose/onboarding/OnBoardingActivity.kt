package com.febrian.icc.ui.screen.compose.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.febrian.icc.ui.MainActivity
import com.febrian.icc.ui.screen.compose.SetupNavGraph
import com.febrian.icc.ui.ui.theme.ICCTheme

class OnBoardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(
                        context = applicationContext,
                        activity = MainActivity(),
                        navController = navController,
                        startDestination = "onboard_screen"
                    )
                }
            }
        }
    }
}
