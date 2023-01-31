package com.febrian.icc.ui.setting.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febrian.icc.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Header()
        Spacer(modifier = Modifier.height(48.dp))
        About()
    }

}

@Composable
fun Header() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Setting App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "Icon Header",
                modifier = Modifier
                    .padding(top = 12.dp, start = 64.dp)
                    .size(156.dp)
            )
        }

    }
}

@Composable
fun About(){

    val stateAbout = remember{ mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_info_24),
            contentDescription = "Info"
        )
        Text(
            text = "About",
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable {
                    stateAbout.value = true
                },
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Black)
    )

    if (stateAbout.value) ShowAbout(state = stateAbout)

}

@Composable
private fun ShowAbout(
    state: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { state.value = false },
        dismissButton = {

        },
        confirmButton = {},
        title = {
            Text(
                text = "Develop By",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Delta Squad",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Delta Squad is an Indonesian based team who developed ICC (International Covid Center) and gives newset covid data globally, most update news and covid handling protocols",
                    color = Color.Black
                )

            }
        },
    )
}