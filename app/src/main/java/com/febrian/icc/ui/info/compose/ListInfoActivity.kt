package com.febrian.icc.ui.info.compose

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.info.ListInfoData
import com.febrian.icc.ui.info.InfoViewModel
import com.febrian.icc.ui.ui.theme.ICCTheme
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

class ListInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val data = intent.getStringExtra(Constant.KEY_INFO)
                    val title = intent.getStringExtra(Constant.KEY_TITLE_INFO).toString()
                    val viewModel =
                        ViewModelFactory.getInstance(this).create(InfoViewModel::class.java)

                    val listInfo =
                        viewModel.getListInfo(data.toString()).observeAsState().value


                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                        item {
                            Title(title, this@ListInfoActivity)
                        }

                        item {
                            if (viewModel.isLoading.observeAsState().value == true) {
                                CircularProgressBar()
                            }
                        }

                        when (listInfo) {
                            is ApiResponse.Success -> {
                                items((listInfo.data.data as List<ListInfoData>) ?: listOf()) {
                                    ItemListInfo(it)
                                }

                            }
                            is ApiResponse.Error -> {
                                showMessage(listInfo.errorMessage, applicationContext)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemListInfo(listInfo: ListInfoData) {

    val stateInfo = remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(24.dp, 8.dp)
            .fillMaxWidth()
            .heightIn(108.dp)
            .clickable {
                stateInfo.value = true
            },
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier

        ) {

            val (image, title, description) = createRefs()

            //Image
            Image(
                painter = rememberAsyncImagePainter(model = listInfo.image),
                contentDescription = "Image",
                modifier = Modifier
                    .width(108.dp)
                    .height(108.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    })

            //Title
            Text(
                text = listInfo.key.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
                    .width(180.dp)
                    .constrainAs(title) {
                        start.linkTo(image.end)
                        top.linkTo(parent.top)
                    }
            )

            //Description
            Text(
                text = listInfo.description.toString(),
                fontSize = 12.sp,
                maxLines = 3,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp)
                    .width(180.dp)
                    .constrainAs(description) {
                        start.linkTo(image.end)
                        top.linkTo(title.bottom)
                    }
            )
        }
    }

    if (stateInfo.value) ShowInfo(state = stateInfo, listInfo)
}

@Composable
private fun ShowInfo(state: MutableState<Boolean>, listInfo: ListInfoData) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 480.dp),
        onDismissRequest = { state.value = false },
        dismissButton = {

        },
        confirmButton = {},

        text = {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top) {
                Text(
                    text = listInfo.key.toString(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Text(
                    text = listInfo.description?.replace("\\n", "\n").toString(),
                    color = Color.Black,
                    fontSize = 14.sp
                )

            }
        },
    )
}