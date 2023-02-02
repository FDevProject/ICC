package com.febrian.icc.ui.home.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.febrian.icc.ui.global.compose.GlobalScreen
import com.febrian.icc.ui.info.compose.InfoScreen
import com.febrian.icc.ui.news.compose.NewsScreen
import com.febrian.icc.ui.setting.compose.SettingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationSetup(
    navController: NavHostController
) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(BottomNavigationMenuItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavigationMenuItem.Global.route) {
            GlobalScreen()
        }
        composable(BottomNavigationMenuItem.News.route) {
            NewsScreen()
        }
        composable(BottomNavigationMenuItem.Info.route) {
            InfoScreen()
        }
        composable(BottomNavigationMenuItem.Setting.route) {
            SettingScreen()
        }
    }
}
