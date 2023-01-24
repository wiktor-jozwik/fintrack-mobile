package com.example.fintrack.view.ui

import android.graphics.Color
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
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentSummaryBinding
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.YearlyOperationsSummaryViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class YearlyOperationsSummaryFragment : Fragment() {
    private val yearlyOperationsSummaryViewModel: YearlyOperationsSummaryViewModel by viewModels()

    private var yearlyOperationsLiveData: MutableLiveData<Triple<Double, Double, Double>> =
        MutableLiveData()

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        yearlyOperationsLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE

        binding.pieChart.setNoDataText("")
        binding.textTitle.text = "${LocalDate.now().year} summary"

        yearlyOperationsLiveData.observe(viewLifecycleOwner) {
            binding.textBalanceProgressBar.visibility = View.INVISIBLE
            binding.pieChartProgressBar.visibility = View.INVISIBLE
            val (totalIncome, totalOutcome, balance) = it

            drawChart(totalIncome, totalOutcome)
            setBalance(balance)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                yearlyOperationsLiveData.value =
                    yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun drawChart(totalIncome: Double, totalOutcome: Double) {
        val pieChart = binding.pieChart

        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.setDrawCenterText(true)
        pieChart.isRotationEnabled = false

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleColor(Color.TRANSPARENT)
        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 30f

        pieChart.legend.isEnabled = false

        val pieEntries =
            listOf(PieEntry(totalIncome.toFloat()), PieEntry(totalOutcome.toFloat()))

        val pieDataSet = PieDataSet(pieEntries, "Expenses")
        pieDataSet.setDrawIcons(false)

        val colors = listOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.main_green
            ),
            ContextCompat.getColor(
                requireContext(),
                R.color.main_red
            )
        )

        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(16f)
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD)
        pieData.setValueTextColor(Color.WHITE)

        pieChart.data = pieData

        pieChart.animateY(1100, Easing.EaseInOutQuad)
        pieChart.invalidate()
    }

    private fun setBalance(balance: Double) {
        binding.textBalanceValue.text = "$balance"
        var balanceColor = R.color.main_green
        if (balance < 0) {
            balanceColor = R.color.main_red
        } else if (balance == 0.0) {
            balanceColor = R.color.text_hint
        }
        binding.textBalanceValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                balanceColor
            )
        )
    }
}