package com.example.fintrack.view.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentChartCurrencyBinding
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.ChartCurrencyViewModel
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
class ChartCurrencyFragment : Fragment(R.layout.fragment_chart_currency) {
    private val chartCurrencyViewModel: ChartCurrencyViewModel by viewModels()

    private var chartLiveData: MutableLiveData<Pair<List<Entry>, List<String>>> =
        MutableLiveData()

    private var currencyLiveData: MutableLiveData<List<Currency>> = MutableLiveData()

    private var chartShown: Boolean = false

    private var _binding: FragmentChartCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChartCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.radioButtonOneYear.isChecked = true
        _binding = null;
        chartLiveData = MutableLiveData()
        chartShown = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fulfillCurrencySpinner()

        setOnSpinnerListener()

        chartLiveData.observe(viewLifecycleOwner) {
            binding.lineChartProgressBar.visibility = View.INVISIBLE
            drawChart(it.first, it.second)
        }
    }

    private fun fulfillCurrencySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            currencyLiveData.observe(viewLifecycleOwner) {
                val currenciesNames = it.map { currency -> currency.name }
                val currenciesAdapter = ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    currenciesNames
                )
                binding.inputCurrency.adapter = currenciesAdapter
                if (!chartShown) {
                    setXYearChart(1)
                    binding.radioButtonOneYear.isChecked = true
                }
                bindChartChange()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                currencyLiveData.value = chartCurrencyViewModel.getSupportedCurrencies()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun setOnSpinnerListener() {
        binding.inputCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                triggerChartByButton(binding.radioGroupPeriod.checkedRadioButtonId)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }

    private fun bindChartChange() {
        binding.lineChart.setNoDataText("")

        binding.radioGroupPeriod.setOnCheckedChangeListener { _, switchId ->
            triggerChartByButton(switchId)
        }
    }

    private fun triggerChartByButton(switchId: Int) {
        binding.lineChart.clear()
        binding.lineChartProgressBar.visibility = View.VISIBLE
        chartShown = true
        when (switchId) {
            binding.radioButtonOneMonth.id -> setXMonthChart(1)
            binding.radioButtonThreeMonth.id -> setXMonthChart(3)
            binding.radioButtonSixMonth.id -> setXMonthChart(6)
            binding.radioButtonOneYear.id -> setXYearChart(1)
            binding.radioButtonThreeYears.id -> setXYearChart(3)
            binding.radioButtonFiveYears.id -> setXYearChart(5)
        }
    }

    private fun setXMonthChart(x: Int) {
        val chosenCurrency = binding.inputCurrency.selectedItem.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                chartLiveData.value =
                    chartCurrencyViewModel.getHistoricalCurrencyPrice(
                        chosenCurrency,
                        x,
                        ChartCurrencyViewModel.Period.MONTH
                    )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun setXYearChart(x: Int) {
        val chosenCurrency = binding.inputCurrency.selectedItem.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                chartLiveData.value =
                    chartCurrencyViewModel.getHistoricalCurrencyPrice(
                        chosenCurrency,
                        x,
                        ChartCurrencyViewModel.Period.YEAR
                    )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun drawChart(currencyPrices: List<Entry>, xLabels: List<String>) {
        val lineChart = binding.lineChart

        lineChart.marker = object : MarkerView(context, R.layout.currency_marker_layout) {
            override fun refreshContent(e: Entry, highlight: Highlight) {
                (findViewById<View>(R.id.tvContent) as TextView).text = "${e.y}"
            }
        }

        val currencyPriceSet = LineDataSet(currencyPrices, "Currency ${binding.inputCurrency.selectedItem}")
        currencyPriceSet.lineWidth = 2f
        currencyPriceSet.setDrawValues(false)
        currencyPriceSet.setDrawCircles(false)
        currencyPriceSet.color = ContextCompat.getColor(requireContext(), R.color.main_green)

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
        xAxis.setAvoidFirstLastClipping(true)

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

        val lineData = LineData(currencyPriceSet)
        lineChart.data = lineData

        lineChart.invalidate()
        lineChart.animateX(1000)
    }
}