package com.example.moneytracker.view.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentPeriodOperationsChartBinding
import com.example.moneytracker.viewmodel.PeriodOperationsChartViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class PeriodOperationsChartFragment : Fragment() {
    private var monthLabels: List<String> =
        listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )


    private val periodOperationsChartViewModel: PeriodOperationsChartViewModel by viewModels()

    private var _binding: FragmentPeriodOperationsChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodOperationsChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setXMonthChart(6)
        binding.radioButtonSixMonth.isChecked = true

        binding.radioGroupPeriod.setOnCheckedChangeListener { _, switchId ->
            when (switchId) {
                binding.radioButtonThreeMonth.id -> setXMonthChart(3)
                binding.radioButtonSixMonth.id -> setXMonthChart(6)
                binding.radioButtonTwelveMonth.id -> setXMonthChart(12)
                binding.radioButtonAllTime.id -> setAllTimeChart()
            }
        }
    }

    private fun setXMonthChart(size: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            periodOperationsChartViewModel.getLastXMonthsOperations(size)
                .observe(viewLifecycleOwner) {
                    val xLabels = getMonthXLabels(size)

                    drawChart(it, size, xLabels)
                }
        }
    }

    private fun setAllTimeChart() {
        viewLifecycleOwner.lifecycleScope.launch {
            periodOperationsChartViewModel.getAllTimeOperations().observe(viewLifecycleOwner) {
                val xLabels = getYearsXLabelsFromOperations(it.first)

                drawChart(it, xLabels.size, xLabels)
            }
        }
    }

    private fun drawChart(
        incomeAndOutcomesBars: Pair<List<BarEntry>, List<BarEntry>>,
        size: Int,
        xLabels: List<String>
    ) {
        val barChart = binding.barChart

        val incomesSet = BarDataSet(incomeAndOutcomesBars.first, "Incomes");
        incomesSet.color = ContextCompat.getColor(requireContext(), R.color.main_green)
        incomesSet.valueTextSize = 13f;
        incomesSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.text)

        val outcomesSet = BarDataSet(incomeAndOutcomesBars.second, "Outcomes");
        outcomesSet.color = ContextCompat.getColor(requireContext(), R.color.main_red)
        outcomesSet.valueTextSize = 13f;
        outcomesSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.text)

        val data = BarData(incomesSet, outcomesSet)
        barChart.axisLeft.axisMinimum = 0f;
        barChart.axisLeft.textSize = 14f;

        barChart.description.isEnabled = false
        barChart.axisRight.axisMinimum = 0f
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setMaxVisibleValueCount(10)
        barChart.setPinchZoom(false)
        barChart.setTouchEnabled(false)
        barChart.setDrawGridBackground(false)
        barChart.extraBottomOffset = size.toFloat()

        val legend = barChart.legend
        legend.isWordWrapEnabled = true
        legend.textSize = 14f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.CIRCLE
        legend.textColor = ContextCompat.getColor(requireContext(), R.color.text)

        val xAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -45f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = size.toFloat()
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

        val groupSpace = 0.06f
        val barSpace = 0.02f
        val barWidth = 0.45f

        data.barWidth = barWidth
        barChart.data = data

        barChart.groupBars(0f, groupSpace, barSpace)
        barChart.invalidate()
        barChart.animateY(700)
    }

    private fun getMonthXLabels(size: Int): List<String> {
        val xLabels = mutableListOf<String>()

        val currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH)
        val firstMonth = (currentMonthIndex - size) + 2

        if (firstMonth <= 0) {
            xLabels += monthLabels.slice(11 + firstMonth..11)
            xLabels += monthLabels.slice(0..currentMonthIndex)
        } else {
            xLabels += monthLabels.slice(firstMonth - 1..currentMonthIndex)
        }
        return xLabels
    }

    private fun getYearsXLabelsFromOperations(yearlyBars: List<BarEntry>): List<String> {
        val xLabels = mutableListOf<String>()

        yearlyBars.forEach {
            xLabels.add(it.x.toInt().toString())
        }

        return xLabels
    }
}
