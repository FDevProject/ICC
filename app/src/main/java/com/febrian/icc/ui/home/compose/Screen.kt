package com.febrian.icc.ui.home.compose

sealed class Screen(val route : String){
    object Home : Screen("home_screen")
    object Global : Screen("global_screen")
    object News : Screen("news_screen")
    object Info : Screen("info_screen")
    object Setting : Screen("setting_screen")
}
