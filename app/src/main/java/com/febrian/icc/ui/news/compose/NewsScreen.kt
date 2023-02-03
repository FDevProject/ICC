package com.febrian.icc.ui.news.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.febrian.icc.R
import com.febrian.icc.data.source.local.EntityNews
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.news.NewsDataResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse
import com.febrian.icc.ui.info.compose.CircularProgressBar
import com.febrian.icc.ui.info.compose.showMessage
import com.febrian.icc.ui.news.BookmarkActivity
import com.febrian.icc.ui.news.DetailNewsActivity
import com.febrian.icc.ui.news.NewsViewModel
import com.febrian.icc.utils.Constant
import com.febrian.icc.utils.ViewModelFactory

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsScreen(
    context: Context = LocalContext.current,
    newsViewModel: NewsViewModel = ViewModelFactory.getInstance(context)
        .create(NewsViewModel::class.java)
) {

    val search = remember {
        mutableStateOf("covid")
    }

    val listNews = remember {
        mutableStateOf(ArrayList<NewsDataResponse>())
    }

    val listStateNews: State<ApiResponse<NewsResponse>?> =
        newsViewModel.getNews(search.value).observeAsState()

    LaunchedEffect(key1 = Unit) {
        newsViewModel.getNews(search.value)
    }

    when (listStateNews.value) {
        is ApiResponse.Success -> {
            listNews.value =
                (listStateNews.value as ApiResponse.Success<NewsResponse>).data.articles
                    ?: arrayListOf()
        }
        is ApiResponse.Error -> {}
        is ApiResponse.Empty -> {}
        else -> {}
    }

    LazyColumn(modifier = Modifier.padding(bottom = 56.dp).fillMaxSize()) {
        item {
            HeaderTitle(context)
        }
        item {
            SearchNews(search)
        }
        item {
            if (newsViewModel.isLoading.observeAsState().value == true) {
                CircularProgressBar()
            }
        }
        items(listNews.value) {
            ItemNews(it, context, newsViewModel)
        }
    }
}

@Composable
fun HeaderTitle(context: Context) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Covid-19 News",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 24.dp, top = 24.dp)
        )

        Image(
            modifier = Modifier
                .padding(end = 24.dp, top = 24.dp)
                .clickable {
                    val intent = Intent(context.applicationContext, BookmarkActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                },
            painter = painterResource(id = R.drawable.ic_baseline_bookmarks_24),
            contentDescription = "Bookmark"
        )
    }
}

@Composable
fun SearchNews(search: MutableState<String>) {
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .padding(8.dp, 0.dp),
        shape = CircleShape,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search icon",
                modifier = Modifier.padding(start = 4.dp),
                tint = Color("#9C9FA8".toColorInt())
            )
        },
        value = search.value,
        placeholder = { Text(text = "Search News") },
        onValueChange = { newText ->
            search.value = newText
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black
        ),

        singleLine = true
    )
}

@Composable
fun ItemNews(news: NewsDataResponse, context: Context, newsViewModel: NewsViewModel) {

    val bookmarkState = newsViewModel.newsExist(news.title.toString()).observeAsState().value

    val state = remember {
        mutableStateOf(bookmarkState)
    }

    val bgBookmark =
        if (state.value == true) R.drawable.ic_baseline_bookmark_24 else R.drawable.ic_baseline_bookmark_border_24

    Card(
        modifier = Modifier
            .padding(24.dp, 8.dp)
            .fillMaxWidth()
            .heightIn(108.dp)
            .clickable {
                val intent = Intent(context.applicationContext, DetailNewsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(Constant.KEY_NEWS, news)
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier

        ) {

            val (image, title, description, bookmark, share) = createRefs()

            //Image
            Image(
                painter = rememberAsyncImagePainter(model = news.urlToImage.toString()),
                contentDescription = "Image",
                modifier = Modifier
                    .width(108.dp)
                    .height(108.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
                contentScale = ContentScale.Crop
            )

            //Title
            Text(
                text = news.title.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
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
                text = news.publishedAt.toString(),
                fontSize = 11.sp,
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

            //Share
            Icon(painter = painterResource(id = R.drawable.ic_baseline_share_24),
                contentDescription = "Share",
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 16.dp)
                    .size(24.dp)
                    .constrainAs(share) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        shareUrl(context, news.url.toString())
                    },tint = Color.Black)

            //Bookmark
            Icon(
                painter = painterResource(id = bgBookmark),
                contentDescription = "Bookmark",
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 16.dp)
                    .size(23.dp)
                    .constrainAs(bookmark) {
                        end.linkTo(share.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {

                        state.value =
                            newsViewModel.newsExistState(news.title.toString(), state).value
                        if (state.value == false) {

                            val entityNews = EntityNews(
                                title = news.title.toString(),
                                url = news.url.toString(),
                                urlToImage = news.urlToImage.toString(),
                                publishedAt = news.publishedAt.toString()
                            )
                            state.value = true
                            newsViewModel.insert(entityNews)

                            showMessage("News success add to bookmark", context)
                        } else {

                            state.value = false
                            newsViewModel.delete(news.title.toString())

                            showMessage("News success delete from bookmark", context)

                        }
                    },
            )
        }
    }
}

private fun shareUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, url)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}