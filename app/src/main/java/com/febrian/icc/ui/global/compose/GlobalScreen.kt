package com.febrian.icc.ui.global.compose

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.ui.global.GlobalViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.maps.OnMapReadyCallback
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GlobalScreen(
    callback: OnMapReadyCallback, viewModel: GlobalViewModel,
    c: Context,
    viewLifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    AndroidView(factory = { context ->

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_global, null, false)

        view // return the view
    }, update = { view ->

        loading(viewModel, view, viewLifecycleOwner)
        observerData(viewModel, view, c, viewLifecycleOwner)
    })
}

private fun observerData(
    viewModel: GlobalViewModel,
    view: View,
    context: Context,
    viewLifecycleOwner: LifecycleOwner
) {
    viewModel.getGlobal().observe(viewLifecycleOwner) {
        when (it) {
            is ApiResponse.Success -> {
                showUIData(it.data, view)
                showDataPieChart(it.data, view, context)
            }
            is ApiResponse.Error -> showMessage(it.errorMessage, context)
            else -> {}
        }
    }
}

private fun loading(viewModel: GlobalViewModel, view: View, viewLifecycleOwner: LifecycleOwner) {
    viewModel.isLoading.observe(viewLifecycleOwner) {
        val loading = view.findViewById<ProgressBar>(R.id.loading)
        if (it) loading.visibility = View.VISIBLE
        else loading.visibility = View.GONE

        view.findViewById<SwipeRefreshLayout>(R.id.refresh_layout).isRefreshing = it
    }

}

private fun showUIData(data: CovidResponse, view: View) {
    view.findViewById<TextView>(R.id.confirmed).text = data.active.toString()
    view.findViewById<TextView>(R.id.recovered).text = data.recovered.toString()
    view.findViewById<TextView>(R.id.deceased).text = data.deaths.toString()

    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    view.findViewById<TextView>(R.id.tgl).text = currentDate
}

private fun showMessage(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun showDataPieChart(data: CovidResponse, view: View, context: Context) {
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
            context,
            android.R.color.transparent
        )
    )

    val pieChart = view.findViewById<PieChart>(R.id.pie_chart)

    pieChart.data = pieData

    pieChart.setUsePercentValues(true)
    pieChart.animateY(1400, Easing.EaseInOutQuad)

    //set text center
    pieChart.centerText = "Total Case\n${NumberFormat.getInstance().format(data.cases)}"
    pieChart.setCenterTextColor(Color.argb(255, 80, 125, 188))
    pieChart.setCenterTextSize(18f)
    pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

    pieChart.legend.isEnabled = false // hide tags labels

    pieChart.setHoleColor(android.R.color.transparent)
    pieChart.setTransparentCircleColor(android.R.color.white)

    pieChart.holeRadius = 75f

    pieChart.setDrawEntryLabels(false)
    pieChart.description.isEnabled = false

}

