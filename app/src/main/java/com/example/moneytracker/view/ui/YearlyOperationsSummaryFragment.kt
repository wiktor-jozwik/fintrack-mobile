package com.example.moneytracker.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel
import kotlinx.android.synthetic.main.fragment_summary.*
import java.util.*
import kotlin.math.abs

class YearlyOperationsSummaryFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel = YearlyOperationsSummaryViewModel()

        val (totalIncome, totalOutcome) = mainViewModel.calculateYearlyIncomeAndOutcome()

        Log.i("total", totalIncome.toString())
        Log.i("total", totalOutcome.toString())

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        tvIncomeText.text = "Your total incomes in ${currentYear}:"
        tvIncomeValue.text = totalIncome.toString()
        tvOutcomeText.text = "Your total outcomes in ${currentYear}:"
        tvOutcomeValue.text = abs(totalOutcome).toString()
    }
}