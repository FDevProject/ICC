package com.febrian.icc.ui.screen.compose

import android.app.Activity
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.febrian.icc.ui.screen.compose.onboarding.OnBoardingScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun SetupNavGraph(
    context: Context,
    activity: Activity,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "onboard_screen") {
            OnBoardingScreen(context, activity)
        }

    }
}