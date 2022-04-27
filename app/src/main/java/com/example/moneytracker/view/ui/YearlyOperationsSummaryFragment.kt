package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel
import kotlinx.android.synthetic.main.fragment_summary.*
import java.time.LocalDateTime

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

        val yearlyOperationsSummaryViewModel = YearlyOperationsSummaryViewModel()

        val (totalIncome, totalOutcome, balance) = yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome()

        tvCurrentYear.text = "Current year ${LocalDateTime.now().year} summary"
        tvIncomesValue.text = totalIncome.toString()
        tvOutcomesValue.text = totalOutcome.toString()
        tvBalanceValue.text = balance.toString()
    }
}