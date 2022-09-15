package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentChartGoldBinding
import com.example.moneytracker.viewmodel.ChartGoldViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChartGoldFragment : Fragment(R.layout.fragment_chart_gold) {
    private val chartGoldViewModel: ChartGoldViewModel by viewModels()

    private var chartLiveData: MutableLiveData<Pair<List<Entry>, List<String>>> =
        MutableLiveData()

    private var chartShown: Boolean = false

    private var _binding: FragmentChartGoldBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChartGoldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        chartLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartLiveData.observe(viewLifecycleOwner) {
            binding.lineChartProgressBar.visibility = View.INVISIBLE
            drawChart(it.first, it.second)
        }

        if (!chartShown) {
            setXYearChart(1)
            binding.radioButtonOneYear.isChecked = true
        }

        binding.lineChart.setNoDataText("")

        binding.radioGroupPeriod.setOnCheckedChangeListener { _, switchId ->
            binding.lineChart.clear()
            binding.lineChartProgressBar.visibility = View.VISIBLE
            chartShown = true

            when (switchId) {
                binding.radioButtonThreeMonth.id -> setXMonthChart(3)
                binding.radioButtonSixMonth.id -> setXMonthChart(6)
                binding.radioButtonOneYear.id -> setXYearChart(1)
                binding.radioButtonThreeYears.id -> setXYearChart(3)
                binding.radioButtonFiveYears.id -> setXYearChart(5)
            }
        }
    }

    private fun setXMonthChart(x: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            chartLiveData.value =
                chartGoldViewModel.getHistoricalGoldPrice(x, ChartGoldViewModel.Period.MONTH)
        }
    }

    private fun setXYearChart(x: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            chartLiveData.value =
                chartGoldViewModel.getHistoricalGoldPrice(x, ChartGoldViewModel.Period.YEAR)
        }
    }

    private fun drawChart(goldPrices: List<Entry>, xLabels: List<String>) {
        val lineChart = binding.lineChart

        lineChart.marker = object : MarkerView(context, R.layout.gold_marker_layout) {
            override fun refreshContent(e: Entry, highlight: Highlight) {
                (findViewById<View>(R.id.tvContent) as TextView).text = "${e.y}"
            }
        }

        val goldPriceSet = LineDataSet(goldPrices, "Gold price (PLN/ounce)")
        goldPriceSet.lineWidth = 2f
        goldPriceSet.setDrawValues(false)
        goldPriceSet.setDrawCircles(false)
        goldPriceSet.color = ContextCompat.getColor(requireContext(), R.color.main_green)

        lineChart.description.isEnabled = false
        lineChart.extraRightOffset = 30f

        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text)
        xAxis.textSize = 12f

        xAxis.setLabelCount(3, true)
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

        val axisRight = lineChart.axisRight
        axisRight.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.textSize = 12f

        val legend = lineChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.LINE
        legend.textSize = 15f
        legend.textColor = ContextCompat.getColor(requireContext(), R.color.text)

        val lineData = LineData(goldPriceSet)
        lineChart.data = lineData

        lineChart.invalidate()
        lineChart.animateX(1000)
    }
}