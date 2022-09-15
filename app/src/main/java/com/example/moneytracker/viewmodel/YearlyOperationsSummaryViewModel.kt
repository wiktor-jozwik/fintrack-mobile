package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.viewmodel.utils.CurrencyCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class YearlyOperationsSummaryViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyCalculator: CurrencyCalculator
) : ViewModel() {
    suspend fun calculateYearlyIncomeAndOutcome(): Triple<Double, Double, Double> {
        val year = LocalDate.now().year
        val startDate = LocalDate.parse("${year}-01-01")
        val endDate = LocalDate.parse("${year}-12-31")
        var yearlyOperations = operationRepository.getAllOperationsInRanges(startDate, endDate).body()
        if (yearlyOperations?.isNullOrEmpty() == true) {
            yearlyOperations = listOf()
        }
        val defaultCurrencyName: String = currencyRepository.getUserDefaultCurrency().body()?.name ?: "PLN"

        val (totalIncomeDecimal, totalOutcomeDecimal) = calculateIncomesAndOutcomes(defaultCurrencyName, yearlyOperations)
        val balanceDecimal = totalIncomeDecimal - totalOutcomeDecimal

        return Triple(
            currencyCalculator.roundMoney(totalIncomeDecimal),
            currencyCalculator.roundMoney(totalOutcomeDecimal),
            currencyCalculator.roundMoney(balanceDecimal)
        )
    }

    private suspend fun calculateIncomesAndOutcomes(defaultCurrencyName: String, operations: List<Operation>): Pair<Double, Double> {
        var incomes = 0.0
        var outcomes = 0.0

        operations.forEach {
            val moneyAmountInDefaultCurrency = currencyRepository.convertCurrency(it.currency.name, defaultCurrencyName, it.moneyAmount, it.date)

            when (it.category.type) {
                CategoryType.INCOME -> incomes += moneyAmountInDefaultCurrency
                CategoryType.OUTCOME -> outcomes += moneyAmountInDefaultCurrency
            }
        }

        return Pair(incomes, outcomes)
    }
}