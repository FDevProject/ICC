package com.febrian.icc.ui.screen.compose

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.febrian.icc.MainActivity
import com.febrian.icc.R
import com.febrian.icc.ui.screen.compose.onboarding.OnBoardingActivity
import com.febrian.icc.ui.ui.theme.ICCTheme
import com.febrian.icc.utils.Constant
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val sharedPreference: SharedPreferences = getSharedPreferences(Constant.PREFERENCE, Context.MODE_PRIVATE)

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.size(128.dp),
                            painter = painterResource(id = R.drawable.logo_big_black),
                            contentDescription = "Logo",
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        androidx.compose.material3.Text(
                            text = "ICC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.Text(
                            text = "International Covid Center",
                            color = Color.Black,
                            fontSize = 24.sp,
                        )
                    }

                    val value = sharedPreference.getString(Constant.KEY_LOG, "")
                    lifecycleScope.launchWhenCreated {
                        delay(1000)
                        if (value == Constant.KEY_LOG) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(applicationContext, OnBoardingActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

            }
        }
    }
}
