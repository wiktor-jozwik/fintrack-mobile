package com.example.moneytracker.view.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moneytracker.R
import com.example.moneytracker.viewmodel.YearlyOperationSummaryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.abs

class YearlyOperationSummaryActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModel = YearlyOperationSummaryViewModel()

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