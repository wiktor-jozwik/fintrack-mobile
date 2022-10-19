package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.viewmodel.utils.CurrencyCalculator
import com.example.moneytracker.viewmodel.utils.ExpenseCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class YearlyOperationsSummaryViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val expenseCalculator: ExpenseCalculator,
    private val currencyCalculator: CurrencyCalculator
) : ViewModel() {
    suspend fun calculateYearlyIncomeAndOutcome(): Triple<Double, Double, Double> {
        val year = LocalDate.now().year
        val startDate = LocalDate.parse("${year}-01-01")
        val endDate = LocalDate.parse("${year}-12-31")
        val yearlyOperations = operationRepository.getAllOperationsInDefaultCurrency(startDate, endDate)

        val (income, outcome) = expenseCalculator.calculate(yearlyOperations)
        val balance = currencyCalculator.roundMoney(income - outcome)

        return Triple(
            income,
            outcome,
            balance
        )
    }
}