package com.febrian.icc.ui.screen.compose.onboarding

import androidx.annotation.DrawableRes
import com.febrian.icc.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title : String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.onb1,
        title = "Hi, Welcome to the International Covid Center!",
        description = "Here you can see the COVID-19 cases update globally."
    )

    object Second : OnBoardingPage(
        image = R.drawable.onb2_2,
        title = "You can also...",
        description = "See COVID stats for any country"
    )

    object Third : OnBoardingPage(
        image = R.drawable.onb3,
        title = "Did We Mention About...",
        description = "You can get covid related news per country"
    )

    object Fourth : OnBoardingPage(
        image = R.drawable.onb4,
        title = "And of course...",
        description = "You can learn covid-19 related information from the best sources"
    )

    object Fifth : OnBoardingPage(
        image = R.drawable.onb5,
        title = "Are you ready?",
        description =   ""
    )
}