package com.example.moneytracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.OperationRepository
import java.time.LocalDateTime
import java.util.*

class YearlyOperationSummaryViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()

    fun calculateYearlyIncomeAndOutcome(): Pair<Double, Double> {
        val year = LocalDateTime.now().year
        val startDate = LocalDateTime.parse("${year}-01-01T00:00:01")
        val endDate = LocalDateTime.parse("${year}-12-31T23:59:59")

        val operationsYearly = operationRepository.getOperationListInRange(startDate, endDate)

        var totalIncome = 0.0
        var totalOutcome = 0.0

        operationsYearly.forEach {
            if (it.moneyAmount > 0) totalIncome += it.moneyAmount * 100
            else totalOutcome += it.moneyAmount * 100
        }

        return Pair(totalIncome / 100, totalOutcome / 100)
    }
}