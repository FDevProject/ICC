package com.febrian.icc.ui.info.compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febrian.icc.R
import com.febrian.icc.utils.Constant

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun InfoScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
    ) {
        item {
            Header()
        }

        item {

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ItemCard(R.drawable.general, "General", "general", 0)
                    ItemCard(R.drawable.dry_cough, "Symptoms", "symptoms", 1, 12.dp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ItemCard(R.drawable.prevention, "Prevention", "prevention", 1)
                    ItemCard(R.drawable.vaccins, "Vaccines", "vaccines", 0, 12.dp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ItemCard(R.drawable.covid_test, "Covid Test", "covid_test", 0)
                    ItemCard(R.drawable.exercise, "Exercise", "exercise", 1, 12.dp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ItemCard(R.drawable.food, "Food", "food", 1)
                    ItemCard(R.drawable.peoplerisk, "People At Risk", "people_risk", 0, 12.dp)
                }
            }
        }

    }

}

@Composable
fun Header() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp),
        elevation = 2.dp
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Hi, How Can I \nHelp You Today?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.bg_header),
                contentDescription = "Icon Header",
                modifier = Modifier
                    .padding(top = 12.dp, start = 64.dp)
                    .size(148.dp)
                    .rotate(-10f)
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ItemCard(
    image: Int = R.drawable.general,
    title: String = "General",
    key: String = "general",
    keyState: Int = 0,
    marginTop: Dp = 0.dp, context: Context = LocalContext.current
) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = marginTop)
            .width(164.dp)
            .heightIn(220.dp)
            .padding(8.dp)
            .clickable {

                fun moveActivity(activity: Activity) {
                    val intent = Intent(context, activity::class.java)
                    intent.putExtra(Constant.KEY_INFO, key)
                    intent.putExtra(Constant.KEY_TITLE_INFO, title)
                    context.startActivity(intent)
                }

                if (keyState == 0) {
                    moveActivity(DropdownInfoActivity())
                } else {
                    moveActivity(ListInfoActivity())
                }

            },
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Icon Header",
                modifier = Modifier
                    .size(108.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

        }

    }
}