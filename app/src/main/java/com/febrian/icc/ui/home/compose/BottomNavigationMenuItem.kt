package com.febrian.icc.ui.home.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavigationMenuItem(
        route = Screen.Home.route,
        title = "Home",
        icon = Icons.Default.Home
    )

    object Global : BottomNavigationMenuItem(
        route = Screen.Global.route,
        title = "Global",
        icon = Icons.Default.Language
    )

    object News : BottomNavigationMenuItem(
        route = Screen.News.route,
        title = "News",
        icon = Icons.Default.Article
    )

    object Info : BottomNavigationMenuItem(
        route = Screen.Info.route,
        title = "Info",
        icon = Icons.Default.Info
    )

    object Setting : BottomNavigationMenuItem(
        route = Screen.Setting.route,
        title = "Setting",
        icon = Icons.Default.Settings
    )

}