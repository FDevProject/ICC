package com.febrian.icc.ui.info.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.info.DropdownInfoData
import com.febrian.icc.data.source.remote.response.info.ListInfoData
import com.febrian.icc.ui.info.InfoViewModel
import com.febrian.icc.ui.ui.theme.ICCTheme
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

class DropdownInfoActivity : ComponentActivity() {
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

                    val listDropDown =
                        viewModel.getDropdownInfo(data.toString()).observeAsState().value


                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Title(title, this@DropdownInfoActivity)
                        }
                        item {
                            if (viewModel.isLoading.observeAsState().value == true) {
                                CircularProgressBar()
                            }
                        }

                        when (listDropDown) {
                            is ApiResponse.Success -> {
                                items((listDropDown.data.data as List<DropdownInfoData>) ?: listOf()) {
                                    ItemDropdown(it)
                                }

                            }
                            is ApiResponse.Error -> {
                                showMessage(listDropDown.errorMessage, applicationContext)
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
fun ItemDropdown(dropdownInfoData: DropdownInfoData) {

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            },
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {

                val (text,image) = createRefs()

                Text(
                    text = dropdownInfoData.key.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 48.dp, start = 12.dp).fillMaxWidth().constrainAs(text){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(image.start)
                    }
                )
                Image(
                    painter = painterResource(id = if (isExpanded) R.drawable.ic_baseline_remove_circle_outline_24 else R.drawable.ic_baseline_add_circle_outline_24),
                    contentDescription = "Expand",
                    modifier = Modifier.constrainAs(image){
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )

            }

            if (isExpanded) {

                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(Color.Black)
                )

                Text(
                    text = dropdownInfoData.value?.replace("\\n", "\n").toString(),
                    fontSize = 12.sp,
                    color = Color.Black
                )

            }
        }
    }
}
