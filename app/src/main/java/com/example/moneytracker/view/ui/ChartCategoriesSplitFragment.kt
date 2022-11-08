package com.example.moneytracker.view.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentChartCategoriesSplitBinding
import com.example.moneytracker.view.ui.datepickers.BaseDatePicker
import com.example.moneytracker.view.ui.datepickers.CategoryEndDatePickerFragment
import com.example.moneytracker.view.ui.datepickers.CategoryStartDatePickerFragment
import com.example.moneytracker.view.ui.datepickers.RequestKey
import com.example.moneytracker.view.ui.utils.isValidDate
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.viewmodel.ChartCategoriesSplitViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class ChartCategoriesSplitFragment : Fragment(R.layout.fragment_chart_categories_split) {
    private val chartCategoriesSplitViewModel: ChartCategoriesSplitViewModel by viewModels()

    @Inject
    lateinit var categoryStartDatePickerFragment: CategoryStartDatePickerFragment

    @Inject
    lateinit var categoryEndDatePickerFragment: CategoryEndDatePickerFragment

    private var chartLiveData: MutableLiveData<Pair<List<String>, List<BarEntry>>> =
        MutableLiveData()

    private var _binding: FragmentChartCategoriesSplitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChartCategoriesSplitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        chartLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.barChart.setNoDataText("")
            chartLiveData.observe(viewLifecycleOwner) {
                binding.barChartProgressBar.visibility = View.INVISIBLE
                drawChart(it.first, it.second)
            }
        }

        setDatePickersListener()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                setChartLiveData(null, null)
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        binding.buttonApply.setOnClickListener {
            refreshChart()
        }
    }

    private fun refreshChart() {
        val startDate = binding.buttonDatePickerStart.text
        val endDate = binding.buttonDatePickerEnd.text

        if (!isValidDate(startDate.toString()) || !isValidDate(endDate.toString())) {
            makeErrorToast(requireContext(), "Specify both start date and end date", 200)
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                setChartLiveData(LocalDate.parse(startDate), LocalDate.parse(endDate))
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private suspend fun setChartLiveData(startDate: LocalDate?, endDate: LocalDate?) {
        val operationsSplitByCategories: Pair<List<String>, List<BarEntry>> =
            chartCategoriesSplitViewModel.getSplitOperationByCategories(startDate, endDate)
        if (operationsSplitByCategories.first.isNotEmpty() && operationsSplitByCategories.second.isNotEmpty()) {
            chartLiveData.value = operationsSplitByCategories
        } else {
            makeErrorToast(requireContext(), "No data for specified range", 200)
        }
    }

    private fun drawChart(xLabels: List<String>, bars: List<BarEntry>) {
        val barChart = binding.barChart

        val categoriesSet = BarDataSet(bars, "Money");
        categoriesSet.color = ContextCompat.getColor(requireContext(), R.color.main_red)
        categoriesSet.valueTextSize = 12f;
        categoriesSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.text)

        val data = BarData(categoriesSet)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > 0) {
                    super.getFormattedValue(value)
                } else {
                    ""
                }
            }
        })

        data.barWidth = 0.65f
        barChart.axisLeft.axisMinimum = 0f;
        barChart.axisLeft.textSize = 14f;

        barChart.description.isEnabled = false
        barChart.axisRight.axisMinimum = 0f
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(false)

        val legend = barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textSize = 15f
        legend.textColor = ContextCompat.getColor(requireContext(), R.color.text)

        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f
        xAxis.axisMaximum = xLabels.size.toFloat()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text)

        xAxis.labelCount = xLabels.size;
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

        val leftAxis = barChart.axisLeft
        leftAxis.removeAllLimitLines()
        leftAxis.typeface = Typeface.DEFAULT
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setDrawGridLines(false)
        leftAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text)

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.data = data
        barChart.setVisibleXRangeMaximum(7f)
        barChart.extraBottomOffset = 2f
        barChart.invalidate()
        barChart.animateY(600)
    }

    private fun setDatePickersListener() {
        binding.apply {
            buttonDatePickerStart.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    RequestKey.value,
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == RequestKey.value) {
                        val date = bundle.getString("CategoryStartDatePickerFragment")
                        buttonDatePickerStart.text = date.toString()
                    }
                }

                categoryStartDatePickerFragment.show(
                    supportFragmentManager,
                    "categoryStartDatePickerFragment"
                )
            }

            buttonDatePickerEnd.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    RequestKey.value,
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == RequestKey.value) {
                        val date = bundle.getString("CategoryEndDatePickerFragment")
                        buttonDatePickerEnd.text = date.toString()
                    }
                }

                categoryEndDatePickerFragment.show(
                    supportFragmentManager,
                    "categoryEndDatePickerFragment"
                )
            }
        }
    }
}