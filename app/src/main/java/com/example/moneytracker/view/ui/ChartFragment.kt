package com.example.moneytracker.view.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentChartBinding
import com.example.moneytracker.viewmodel.ChartViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ChartFragment : Fragment() {
    var xAxisValues: List<String> =
        listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )


    private val chartViewModel: ChartViewModel by viewModels()

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setThreeMonthChart()
        binding.radioGroupPeriod.setOnCheckedChangeListener { _, switchId ->
            when (switchId) {
                binding.radioButtonThreeMonth.id -> setThreeMonthChart()
                binding.radioButtonSixMonth.id -> setSixMonthChart()
                binding.radioButtonTwelveMonth.id -> setTwelveMonthChart()
                binding.radioButtonAllTime.id -> setAllTimeChart()
            }
        }

//        initChart()
    }

    private fun setThreeMonthChart() {
        viewLifecycleOwner.lifecycleScope.launch {
            chartViewModel.getLastXMonthsOperations(3).observe(viewLifecycleOwner) {
                Log.d("MT", it.first.toString())
                Log.d("MT", it.second.toString())
                val barChart = binding.barChart
                val set1 = BarDataSet(it.first, "Incomes");
                set1.setColor(ContextCompat.getColor(requireContext(), R.color.green))
                set1.setValueTextSize(10f);

                val set2 = BarDataSet(it.second, "Outcomes");
                set2.setColor(ContextCompat.getColor(requireContext(), R.color.red))
                set2.setValueTextSize(10f);

                val data = BarData(set1, set2)
                barChart.getAxisLeft().setAxisMinimum(0f);

                barChart.description.isEnabled = false
                barChart.axisRight.axisMinimum = 0f
                barChart.setDrawBarShadow(false)
                barChart.setDrawValueAboveBar(true)
                barChart.setMaxVisibleValueCount(10)
                barChart.setPinchZoom(false)
                barChart.setDrawGridBackground(false)


                val l = barChart.legend
                l.isWordWrapEnabled = true
                l.textSize = 14f
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)
                l.form = Legend.LegendForm.CIRCLE

                val xAxis = barChart.xAxis
                xAxis.granularity = 1f
                xAxis.setCenterAxisLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.labelRotationAngle = -45f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMaximum = 3f


                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues.slice(2..5))


                val leftAxis = barChart.axisLeft
                leftAxis.removeAllLimitLines()
                leftAxis.typeface = Typeface.DEFAULT
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                leftAxis.textColor = Color.BLACK
                leftAxis.setDrawGridLines(false)
                barChart.axisRight.isEnabled = false

                val groupSpace = 0.06f
                val barSpace = 0.02f
                val barWidth = 0.45f
                binding.barChart.getXAxis().setAxisMinimum(0f)
                binding.barChart.getXAxis().setAxisMaximum(3f)

                binding.barChart.getXAxis().setCenterAxisLabels(true)

                data.barWidth = barWidth
                barChart.data = data

                binding.barChart.groupBars(0f, groupSpace, barSpace)
                binding.barChart.invalidate()
                barChart.invalidate()
            }
        }
    }

    private fun setSixMonthChart() {
        viewLifecycleOwner.lifecycleScope.launch {
            chartViewModel.getLastXMonthsOperations(6).observe(viewLifecycleOwner) {
                Log.d("MT", it.first.toString())
                Log.d("MT", it.second.toString())
                val barChart = binding.barChart
                val set1 = BarDataSet(it.first, "Incomes");
                set1.setColor(ContextCompat.getColor(requireContext(), R.color.green))
                set1.setValueTextSize(10f);

                val set2 = BarDataSet(it.second, "Outcomes");
                set2.setColor(ContextCompat.getColor(requireContext(), R.color.red))
                set2.setValueTextSize(10f);

                val data = BarData(set1, set2)
                barChart.getAxisLeft().setAxisMinimum(0f);

                barChart.description.isEnabled = false
                barChart.axisRight.axisMinimum = 0f
                barChart.setDrawBarShadow(false)
                barChart.setDrawValueAboveBar(true)
                barChart.setMaxVisibleValueCount(10)
                barChart.setPinchZoom(false)
                barChart.setDrawGridBackground(false)


                val l = barChart.legend
                l.isWordWrapEnabled = true
                l.textSize = 14f
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)
                l.form = Legend.LegendForm.CIRCLE

                val xAxis = barChart.xAxis
                xAxis.granularity = 1f
                xAxis.setCenterAxisLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.labelRotationAngle = -45f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMaximum = 6f

                val newXAxisLabels = mutableListOf(xAxisValues.last())
                newXAxisLabels += xAxisValues.slice(0..4)
                Log.d("MT", newXAxisLabels.toString())
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(newXAxisLabels)


                val leftAxis = barChart.axisLeft
                leftAxis.removeAllLimitLines()
                leftAxis.typeface = Typeface.DEFAULT
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                leftAxis.textColor = Color.BLACK
                leftAxis.setDrawGridLines(false)
                barChart.axisRight.isEnabled = false

                val groupSpace = 0.06f
                val barSpace = 0.02f
                val barWidth = 0.45f
                binding.barChart.getXAxis().setAxisMinimum(0f)
                binding.barChart.getXAxis().setAxisMaximum(6f)

                binding.barChart.getXAxis().setCenterAxisLabels(true)

                data.barWidth = barWidth
                barChart.data = data

                binding.barChart.groupBars(0f, groupSpace, barSpace)
                binding.barChart.invalidate()
                barChart.invalidate()
            }
        }
    }

    private fun setTwelveMonthChart() {
        viewLifecycleOwner.lifecycleScope.launch {
            chartViewModel.getLastXMonthsOperations(12).observe(viewLifecycleOwner) {
                Log.d("MT", 12.toString())

                Log.d("MT", it.first.toString())
                Log.d("MT", it.second.toString())
                val barChart = binding.barChart
                val set1 = BarDataSet(it.first, "Incomes");
                set1.setColor(ContextCompat.getColor(requireContext(), R.color.green))
                set1.setValueTextSize(10f);

                val set2 = BarDataSet(it.second, "Outcomes");
                set2.setColor(ContextCompat.getColor(requireContext(), R.color.red))
                set2.setValueTextSize(10f);

                val data = BarData(set1, set2)
                barChart.getAxisLeft().setAxisMinimum(0f);

                barChart.description.isEnabled = false
                barChart.axisRight.axisMinimum = 0f
                barChart.setDrawBarShadow(false)
                barChart.setDrawValueAboveBar(true)
                barChart.setMaxVisibleValueCount(10)
                barChart.setPinchZoom(false)
                barChart.setDrawGridBackground(false)


                val l = barChart.legend
                l.isWordWrapEnabled = true
                l.textSize = 14f
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)
                l.form = Legend.LegendForm.CIRCLE

                val xAxis = barChart.xAxis
                xAxis.granularity = 1f
                xAxis.setCenterAxisLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.labelRotationAngle = -45f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMaximum = 12f

                val newXAxisLabels = xAxisValues.slice(5..11) +xAxisValues.slice(0..4)
                Log.d("MT", newXAxisLabels.toString())
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(newXAxisLabels)


                val leftAxis = barChart.axisLeft
                leftAxis.removeAllLimitLines()
                leftAxis.typeface = Typeface.DEFAULT
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                leftAxis.textColor = Color.BLACK
                leftAxis.setDrawGridLines(false)
                barChart.axisRight.isEnabled = false

                val groupSpace = 0.06f
                val barSpace = 0.02f
                val barWidth = 0.45f
                binding.barChart.getXAxis().setAxisMinimum(0f)
                binding.barChart.getXAxis().setAxisMaximum(12f)

                binding.barChart.getXAxis().setCenterAxisLabels(true)

                data.barWidth = barWidth
                barChart.data = data

                binding.barChart.groupBars(0f, groupSpace, barSpace)
                binding.barChart.invalidate()
                barChart.invalidate()
            }
        }
    }

//    private fun setAllTimeChart() {
//
//    }

    private fun setAllTimeChart() {

        viewLifecycleOwner.lifecycleScope.launch {
            chartViewModel.getAllTimeOperations().observe(viewLifecycleOwner) {
                val barChart = binding.barChart
//                Log.d("MT", it.first.toString())

                val set1 = BarDataSet(it.first, "Incomes");
                set1.setColor(ContextCompat.getColor(requireContext(), R.color.green))
                set1.setValueTextSize(10f);

                val set2 = BarDataSet(it.second, "Outcomes");
                set2.setColor(ContextCompat.getColor(requireContext(), R.color.red))
                set2.setValueTextSize(10f);

                val data = BarData(set1, set2)

                val l = barChart.legend
                l.isWordWrapEnabled = true
                l.textSize = 14f
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)
                l.form = Legend.LegendForm.CIRCLE

                val xAxis = barChart.xAxis
                xAxis.granularity = 1f
                xAxis.setCenterAxisLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.labelRotationAngle = -45f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMaximum = 3f

                val xAxLabels = listOf("2020", "2021", "2022")

                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxLabels)


                val leftAxis = barChart.axisLeft
                leftAxis.removeAllLimitLines()
                leftAxis.typeface = Typeface.DEFAULT
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                leftAxis.textColor = Color.BLACK
                leftAxis.setDrawGridLines(false)
                barChart.axisRight.isEnabled = false

                val groupSpace = 0.06f
                val barSpace = 0.02f
                val barWidth = 0.45f
                binding.barChart.getXAxis().setAxisMinimum(0f)
                binding.barChart.getXAxis().setAxisMaximum(3f)

                binding.barChart.getXAxis().setCenterAxisLabels(true)

                data.barWidth = barWidth
                barChart.data = data

                binding.barChart.groupBars(0f, groupSpace, barSpace)
                binding.barChart.invalidate()
                barChart.invalidate()






















//                val firstYear = it.first[0].x
//
//                val groupSpace = 0.06f
//                val barSpace = 0.02f
//                val barWidth = 0.45f
//
//
//                val set1 = BarDataSet(it.first, "Incomes");
//                set1.setColor(ContextCompat.getColor(requireContext(), R.color.green))
//                set1.setValueTextSize(10f);
//
//                val set2 = BarDataSet(it.second, "Outcomes");
//                set2.setColor(ContextCompat.getColor(requireContext(), R.color.red))
//                set2.setValueTextSize(10f);
//
//                val data = BarData(set1, set2)
//                data.barWidth = barWidth
//
////                val xAxis: XAxis = barChart.getXAxis()
//
//                val xAxisLabel = mutableListOf<String>()
//                xAxisLabel.add("2020")
//                xAxisLabel.add("2021")
//                xAxisLabel.add("2022")
//                barChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(xAxisLabel));
//
//
//                barChart.data = data
//                barChart.groupBars((firstYear - 0.5).toFloat(), groupSpace, barSpace)
//
//                barChart.invalidate()
            }
        }
    }
}
