package com.febrian.icc.ui.home.compose

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.febrian.icc.ui.global.GlobalViewModel
import com.febrian.icc.ui.global.compose.GlobalScreen
import com.febrian.icc.ui.home.HomeViewModel
import com.febrian.icc.ui.news.NewsViewModel
import com.febrian.icc.ui.news.compose.NewsScreen
import com.febrian.icc.utils.ViewModelFactory
import com.google.android.gms.maps.OnMapReadyCallback

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationSetup(
    navController: NavHostController,
    context: Context,
    callback: OnMapReadyCallback
) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(BottomNavigationMenuItem.Home.route) {
            HomeScreen(
                context,
                ViewModelFactory.getInstance(context).create(HomeViewModel::class.java)
            )
        }
        composable(BottomNavigationMenuItem.Global.route) {
            GlobalScreen(
                callback = callback, viewModel = ViewModelFactory.getInstance(context).create(
                    GlobalViewModel::class.java
                ), c = context
            )
        }
        composable(BottomNavigationMenuItem.News.route) {
            NewsScreen(
                ViewModelFactory.getInstance(context).create(NewsViewModel::class.java),
                context
            )
        }
        composable(BottomNavigationMenuItem.Info.route) {

        }
        composable(BottomNavigationMenuItem.Setting.route) {

        }
    }
}
