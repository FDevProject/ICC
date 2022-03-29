package com.febrian.icc.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse
import com.febrian.icc.databinding.FragmentHomeBinding
import com.febrian.icc.utils.DateUtils.getDateStatistic
import com.febrian.icc.utils.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val homeViewModel = ViewModelFactory.getInstance(requireContext()).create(HomeViewModel::class.java)

            main(homeViewModel)

            binding.swiperefresh.setOnRefreshListener {
                binding.searchView.setQuery("", false)
                main(homeViewModel)
            }
        }
    }

    private fun main(homeViewModel: HomeViewModel){
        loading(homeViewModel)
        searchCountry(homeViewModel)
        observerData(homeViewModel, "Indonesia")
        observerStatistic(homeViewModel, "Indonesia")
    }

    private fun searchCountry(homeViewModel: HomeViewModel) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                observerData(homeViewModel, query.toString())
                observerStatistic(homeViewModel, query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                observerData(homeViewModel, newText.toString())
                observerStatistic(homeViewModel, newText.toString())
                return true
            }
        })
    }

    private fun observerData(homeViewModel: HomeViewModel, country: String) {
        homeViewModel.getCountry(country).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    showUIData(it.data)
                    showDataPieChart(it.data, country)
                }
                is ApiResponse.Error -> showMessage(it.errorMessage)
                else -> {}
            }
        }
    }

    private fun showUIData(data: CovidResponse) {
        binding.confirmedValue.text = data.active.toString()
        binding.recoveredValue.text = data.recovered.toString()
        binding.deathValue.text = data.deaths.toString()
        binding.totalCaseValue.text = data.cases.toString()

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tgl.text = currentDate
    }

    private fun showDataPieChart(data: CovidResponse, country: String) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()

        listPie.add(PieEntry(data.active.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.yellow_primary))
        listPie.add(PieEntry(data.recovered.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.green_custom))
        listPie.add(PieEntry(data.deaths.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.red_custom))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //set text center
        binding.pieChart.centerText = country.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        binding.pieChart.setCenterTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        binding.pieChart.setCenterTextSize(22f)
        binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        binding.pieChart.legend.isEnabled = false // hide tags labels

        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setTransparentCircleColor(android.R.color.white)

        binding.pieChart.holeRadius = 75f

        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false

    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(homeViewModel: HomeViewModel) {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) binding.loading.visibility = View.VISIBLE
            else binding.loading.visibility = View.GONE

            binding.swiperefresh.isRefreshing = it
        }
    }

    private fun observerStatistic(homeViewModel: HomeViewModel, query: String) {
        homeViewModel.getStatistic(query).observe(viewLifecycleOwner) { data ->
            when (data) {
                is ApiResponse.Success -> {
                    showStatistic(data.data)
                }
                is ApiResponse.Error -> {
                    showMessage(data.errorMessage)
                }
                else -> {}
            }
        }
    }

    private fun showStatistic(data: StatisticResponse) {
        blue()
        setStatistic(data.listCases!!, R.color.blue_primary)
        binding.totalCaseStatistic.setOnClickListener {
            blue()
            setStatistic(data.listCases, R.color.blue_primary)
        }
        binding.confirmedStatistic.setOnClickListener {
            yellow()
            setStatistic(data.listActive!!, R.color.yellow_primary)
        }
        binding.recoveredStatistic.setOnClickListener {
            green()
            setStatistic(data.listRecovered!!, R.color.green_custom)
        }
        binding.deceasedStatistic.setOnClickListener {
            red()
            setStatistic(data.listDeath!!, R.color.red_custom)
        }
    }

    private fun setStatistic(listCases: ArrayList<Entry>, color: Int) {
        val casesLineDataSet = LineDataSet(listCases, "Cases")
        casesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        casesLineDataSet.color = ContextCompat.getColor(requireContext(), R.color.bgColorPrimary)
        casesLineDataSet.setCircleColor(ContextCompat.getColor(requireContext(), color))
        casesLineDataSet.setDrawValues(false)
        casesLineDataSet.setDrawFilled(false)
        casesLineDataSet.disableDashedLine()
        casesLineDataSet.setColors(ContextCompat.getColor(requireContext(), color))

        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.setPinchZoom(false)
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.setDrawBorders(false)

        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.axisLeft.setDrawAxisLine(false)

        binding.lineChart.xAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        binding.lineChart.xAxis.textSize = 12f

        binding.lineChart.axisLeft.textColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        binding.lineChart.axisLeft.textSize = 12f

        //Setup Legend
        val legend = binding.lineChart.legend
        legend.isEnabled = false

        val listDate = ArrayList<String>()
        for (i in 9 downTo 2) {
            listDate.add(getDateStatistic(-i))
        }

        val date = AxisDateFormatter(listDate.toArray(arrayOfNulls<String>(listDate.size)))
        binding.lineChart.xAxis?.valueFormatter = date

        binding.lineChart.description.isEnabled = false
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.spaceMax = 0.5f

        binding.lineChart.data = LineData(casesLineDataSet)
        binding.lineChart.animateY(100, Easing.Linear)
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

    private fun blue() {
        setBlue()
        resetYellow()
        resetGreen()
        resetRed()
    }

    private fun yellow() {
        setYellow()
        resetGreen()
        resetRed()
        resetBlue()
    }

    private fun green() {
        setGreen()
        resetRed()
        resetYellow()
        resetBlue()
    }

    private fun red() {
        setRed()
        resetBlue()
        resetYellow()
        resetGreen()
    }

    private fun setBlue() {
        binding.circleBlue.visibility = View.GONE
        binding.textBlue.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_weekly_blue)
        binding.textBlue.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }


    private fun resetBlue() {
        binding.circleBlue.visibility = View.VISIBLE
        binding.textBlue.background =
            ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        binding.textBlue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
    }

    private fun setYellow() {
        binding.icYellow.visibility = View.GONE
        binding.textYellow.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_weekly_yellow)
        binding.textYellow.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
    }

    private fun resetYellow() {
        binding.icYellow.visibility = View.VISIBLE
        binding.textYellow.background =
            ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        binding.textYellow.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
    }

    private fun setGreen() {
        binding.icGreen.visibility = View.GONE
        binding.textGreen.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_weekly_green)
        binding.textGreen.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun resetGreen() {
        binding.icGreen.visibility = View.VISIBLE
        binding.textGreen.background =
            ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        binding.textGreen.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
    }

    private fun setRed() {
        binding.icRed.visibility = View.GONE
        binding.textRed.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_weekly_red)
        binding.textRed.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
    }

    private fun resetRed() {
        binding.icRed.visibility = View.VISIBLE
        binding.textRed.background =
            ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        binding.textRed.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}