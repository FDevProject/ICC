package com.febrian.icc.ui.home.compose

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse
import com.febrian.icc.ui.home.HomeViewModel
import com.febrian.icc.utils.DateUtils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    c: Context,
    homeViewModel: HomeViewModel,
    viewLifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    AndroidView(factory = { context ->

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, null, false)

        view // return the view
    }, update = { view ->

        loading(homeViewModel, view, viewLifecycleOwner)
        searchCountry(view, c, homeViewModel, viewLifecycleOwner)
        observerData("Indonesia", homeViewModel, viewLifecycleOwner, view, c)
        observerStatistic("Indonesia", homeViewModel, viewLifecycleOwner, view, c)
    })
}

private fun searchCountry(
    view: View, context: Context, homeViewModel: HomeViewModel, viewLifecycleOwner: LifecycleOwner
) {
    view.findViewById<SearchView>(R.id.searchView)
        .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                observerData(query.toString(), homeViewModel, viewLifecycleOwner, view, context)
                observerStatistic(
                    query.toString(), homeViewModel, viewLifecycleOwner, view, context
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                observerData(newText.toString(), homeViewModel, viewLifecycleOwner, view, context)
                observerStatistic(
                    newText.toString(), homeViewModel, viewLifecycleOwner, view, context
                )
                return true
            }
        })
}

private fun observerData(
    country: String,
    homeViewModel: HomeViewModel,
    viewLifecycleOwner: LifecycleOwner,
    view: View,
    context: Context
) {
    homeViewModel.getCountry(country).observe(viewLifecycleOwner) {
        when (it) {
            is ApiResponse.Success -> {
                showUIData(it.data, view)
                showDataPieChart(it.data, country, view, context)
            }
            is ApiResponse.Error -> showMessage(it.errorMessage, context)
            else -> {}
        }
    }
}

private fun showMessage(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun showDataPieChart(data: CovidResponse, country: String, view: View, context: Context) {
    val listPie = ArrayList<PieEntry>()
    val listColors = ArrayList<Int>()

    listPie.add(PieEntry(data.active.toFloat()))
    listColors.add(ContextCompat.getColor(context, R.color.yellow_primary))
    listPie.add(PieEntry(data.recovered.toFloat()))
    listColors.add(ContextCompat.getColor(context, R.color.green_custom))
    listPie.add(PieEntry(data.deaths.toFloat()))
    listColors.add(ContextCompat.getColor(context, R.color.red_custom))

    val pieDataSet = PieDataSet(listPie, "")
    pieDataSet.colors = listColors

    val pieData = PieData(pieDataSet)
    pieData.setValueTextSize(0f)
    pieData.setValueTextColor(
        ContextCompat.getColor(
            context, android.R.color.transparent
        )
    )

    val pieChart = view.findViewById<com.github.mikephil.charting.charts.PieChart>(R.id.pieChart)

    pieChart.data = pieData

    pieChart.setUsePercentValues(true)
    pieChart.animateY(1400, Easing.EaseInOutQuad)

    //set text center
    pieChart.centerText = country.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
    pieChart.setCenterTextColor(
        ContextCompat.getColor(
            context, R.color.colorPrimary
        )
    )
    pieChart.setCenterTextSize(22f)
    pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

    pieChart.legend.isEnabled = false // hide tags labels

    pieChart.setHoleColor(android.R.color.transparent)
    pieChart.setTransparentCircleColor(android.R.color.white)

    pieChart.holeRadius = 75f

    pieChart.setDrawEntryLabels(false)
    pieChart.description.isEnabled = false

}

private fun showUIData(data: CovidResponse, view: View) {
    view.findViewById<TextView>(R.id.confirmed_value).text = data.active.toString()
    view.findViewById<TextView>(R.id.recovered_value).text = data.recovered.toString()
    view.findViewById<TextView>(R.id.death_value).text = data.deaths.toString()
    view.findViewById<TextView>(R.id.total_case_value).text = data.cases.toString()

    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    view.findViewById<TextView>(R.id.tgl).text = currentDate
}

private fun observerStatistic(
    query: String,
    homeViewModel: HomeViewModel,
    viewLifecycleOwner: LifecycleOwner,
    view: View,
    context: Context
) {

    homeViewModel.getStatistic(query).observe(viewLifecycleOwner) { data ->
        when (data) {
            is ApiResponse.Success -> {
                showStatistic(data.data, view, context)
            }
            is ApiResponse.Error -> {
                showMessage(data.errorMessage, context)
            }
            else -> {}
        }
    }
}

private fun loading(homeViewModel: HomeViewModel, view: View, viewLifecycleOwner: LifecycleOwner) {

    val loading = view.findViewById<ProgressBar>(R.id.loading)

    homeViewModel.isLoading.observe(viewLifecycleOwner) {
        if (it) loading.visibility = View.VISIBLE
        else loading.visibility = View.GONE

        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).isRefreshing = it
    }

}

private fun showStatistic(data: StatisticResponse, view: View, context: Context) {
    blue(view, context)
    setStatistic(data.listCases!!, R.color.blue_primary, view, context)
    view.findViewById<LinearLayout>(R.id.total_case_statistic).setOnClickListener {
        blue(view, context)
        setStatistic(data.listCases, R.color.blue_primary, view, context)
    }
    view.findViewById<LinearLayout>(R.id.confirmed_statistic).setOnClickListener {
        yellow(view, context)
        setStatistic(data.listActive!!, R.color.yellow_primary, view, context)
    }
    view.findViewById<LinearLayout>(R.id.recovered_statistic).setOnClickListener {
        green(view, context)
        setStatistic(data.listRecovered!!, R.color.green_custom, view, context)
    }
    view.findViewById<LinearLayout>(R.id.deceased_statistic).setOnClickListener {
        red(view, context)
        setStatistic(data.listDeath!!, R.color.red_custom, view, context)
    }
}

private fun setStatistic(listCases: ArrayList<Entry>, color: Int, view: View, context: Context) {
    val lineChart = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
    val casesLineDataSet = LineDataSet(listCases, "Cases")
    casesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
    casesLineDataSet.color = ContextCompat.getColor(context, R.color.bgColorPrimary)
    casesLineDataSet.setCircleColor(ContextCompat.getColor(context, color))
    casesLineDataSet.setDrawValues(false)
    casesLineDataSet.setDrawFilled(false)
    casesLineDataSet.disableDashedLine()
    casesLineDataSet.setColors(ContextCompat.getColor(context, color))

    lineChart.setTouchEnabled(false)
    lineChart.axisRight.isEnabled = false
    lineChart.setPinchZoom(false)
    lineChart.setDrawGridBackground(false)
    lineChart.setDrawBorders(false)

    lineChart.xAxis.setDrawGridLines(false)
    lineChart.axisLeft.setDrawAxisLine(false)

    lineChart.xAxis.textColor = ContextCompat.getColor(context, R.color.colorPrimary)
    lineChart.xAxis.textSize = 12f

    lineChart.axisLeft.textColor = ContextCompat.getColor(context, R.color.colorPrimary)
    lineChart.axisLeft.textSize = 12f

    //Setup Legend
    val legend = lineChart.legend
    legend.isEnabled = false

    val listDate = ArrayList<String>()
    for (i in 9 downTo 2) {
        listDate.add(DateUtils.getDateStatistic(-i))
    }

    val date = AxisDateFormatter(listDate.toArray(arrayOfNulls<String>(listDate.size)))
    lineChart.xAxis?.valueFormatter = date

    lineChart.description.isEnabled = false
    lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    lineChart.xAxis.spaceMax = 0.5f

    lineChart.data = LineData(casesLineDataSet)
    lineChart.animateY(100, Easing.Linear)
}

class AxisDateFormatter(private val mValues: Array<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value >= 0) {
            if (mValues.size > value.toInt()) {
                mValues[value.toInt()]
            } else ""
        } else {
            ""
        }
    }
}

private fun blue(binding: View, context: Context) {
    setBlue(binding, context)
    resetYellow(binding, context)
    resetGreen(binding, context)
    resetRed(binding, context)
}

private fun yellow(binding: View, context: Context) {
    setYellow(binding, context)
    resetGreen(binding, context)
    resetRed(binding, context)
    resetBlue(binding, context)
}

private fun green(binding: View, context: Context) {
    setGreen(binding, context)
    resetRed(binding, context)
    resetYellow(binding, context)
    resetBlue(binding, context)
}

private fun red(binding: View, context: Context) {
    setRed(binding, context)
    resetBlue(binding, context)
    resetYellow(binding, context)
    resetGreen(binding, context)
}

private fun setBlue(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.circle_blue).visibility = View.GONE
    binding.findViewById<TextView>(R.id.text_blue).background =
        ContextCompat.getDrawable(context, R.drawable.bg_weekly_blue)
    binding.findViewById<TextView>(R.id.text_blue)
        .setTextColor(ContextCompat.getColor(context, R.color.white))
}


private fun resetBlue(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.circle_blue).visibility = View.VISIBLE
    binding.findViewById<TextView>(R.id.text_blue).background =
        ContextCompat.getDrawable(context, android.R.color.transparent)
    binding.findViewById<TextView>(R.id.text_blue).setTextColor(
        ContextCompat.getColor(
            context, R.color.colorPrimary
        )
    )
}

private fun setYellow(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_yellow).visibility = View.GONE
    binding.findViewById<TextView>(R.id.text_yellow).background =
        ContextCompat.getDrawable(context, R.drawable.bg_weekly_yellow)
    binding.findViewById<TextView>(R.id.text_yellow).setTextColor(
        ContextCompat.getColor(
            context, android.R.color.white
        )
    )
}

private fun resetYellow(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_yellow).visibility = View.VISIBLE
    binding.findViewById<TextView>(R.id.text_yellow).background =
        ContextCompat.getDrawable(context, android.R.color.transparent)
    binding.findViewById<TextView>(R.id.text_yellow).setTextColor(
        ContextCompat.getColor(
            context, R.color.colorPrimary
        )
    )
}

private fun setGreen(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_green).visibility = View.GONE
    binding.findViewById<TextView>(R.id.text_green).background =
        ContextCompat.getDrawable(context, R.drawable.bg_weekly_green)
    binding.findViewById<TextView>(R.id.text_green)
        .setTextColor(ContextCompat.getColor(context, R.color.white))
}

private fun resetGreen(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_green).visibility = View.VISIBLE
    binding.findViewById<TextView>(R.id.text_green).background =
        ContextCompat.getDrawable(context, android.R.color.transparent)
    binding.findViewById<TextView>(R.id.text_green).setTextColor(
        ContextCompat.getColor(
            context, R.color.colorPrimary
        )
    )
}

private fun setRed(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_red).visibility = View.GONE
    binding.findViewById<TextView>(R.id.text_red).background =
        ContextCompat.getDrawable(context, R.drawable.bg_weekly_red)
    binding.findViewById<TextView>(R.id.text_red).setTextColor(
        ContextCompat.getColor(
            context, android.R.color.white
        )
    )
}

private fun resetRed(binding: View, context: Context) {
    binding.findViewById<ImageView>(R.id.ic_red).visibility = View.VISIBLE
    binding.findViewById<TextView>(R.id.text_red).background =
        ContextCompat.getDrawable(context, android.R.color.transparent)
    binding.findViewById<TextView>(R.id.text_red)
        .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
}

