package com.example.moneytracker.view.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentCategoriesSplitChartBinding
import com.example.moneytracker.viewmodel.CategoriesSplitChartViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoriesSplitChartFragment : Fragment(R.layout.fragment_categories_split_chart) {
    private val categoriesSplitChartViewModel: CategoriesSplitChartViewModel by viewModels()

    private var _binding: FragmentCategoriesSplitChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesSplitChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesSplitChartViewModel.getSplitOperationByCategories()
                .observe(viewLifecycleOwner) {
                    Log.d("MT", it.toString())
                    drawChart(it.first, it.second)
                }
        }
    }

    private fun drawChart(xLabels: List<String>, bars: List<BarEntry>) {
        val barChart = binding.barChart

        val categoriesSet = BarDataSet(bars, "Categories");
        categoriesSet.color = ContextCompat.getColor(requireContext(), R.color.main_red)
        categoriesSet.valueTextSize = 12f;
        categoriesSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.text)

        val data = BarData(categoriesSet)
        barChart.axisLeft.axisMinimum = 0f;
        barChart.axisLeft.textSize = 14f;

        barChart.description.isEnabled = false
        barChart.axisRight.axisMinimum = 0f
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(false)

        barChart.legend.isEnabled = false

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
        barChart.setVisibleXRangeMaximum(8f)
        barChart.invalidate()
        barChart.animateY(700)
    }
}