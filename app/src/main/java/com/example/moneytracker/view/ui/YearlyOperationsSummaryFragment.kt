package com.example.moneytracker.view.ui

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.viewmodel.YearlyOperationsSummaryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_summary.*
import java.time.LocalDateTime

class YearlyOperationsSummaryFragment: Fragment(R.layout.fragment_summary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearlyOperationsSummaryViewModel = YearlyOperationsSummaryViewModel()

        val (totalIncome, totalOutcome, balance) = yearlyOperationsSummaryViewModel.calculateYearlyIncomeAndOutcome()

        textIncomesValue.text = "$totalIncome zł"
        textOutcomesValue.text = "$totalOutcome zł"
        textBalanceValue.text = "$balance zł"


        val addOperationFragment = AddOperationFragment()

        buttonAddNew.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, addOperationFragment)
                commit()
            }
        }
    }
}