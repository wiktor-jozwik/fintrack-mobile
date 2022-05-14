package com.example.moneytracker.view.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel
import kotlinx.android.synthetic.main.fragment_summary.*

class YearlyOperationsSummaryFragment: Fragment(R.layout.fragment_summary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearlyOperationsSummaryViewModel = YearlyOperationsSummaryViewModel()

        yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome().observe(viewLifecycleOwner) {
            val (totalIncome, totalOutcome, balance) = it
            textIncomesValue.text = "$totalIncome zł"
            textOutcomesValue.text = "$totalOutcome zł"
            textBalanceValue.text = "$balance zł"
        }

        val addOperationFragment = AddOperationFragment()

        buttonAddNew.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, addOperationFragment)
                commit()
            }
        }
    }
}