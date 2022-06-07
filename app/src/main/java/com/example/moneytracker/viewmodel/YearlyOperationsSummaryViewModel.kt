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
        val yearlyOperations = operationRepository.getAllOperationsInRanges(startDate, endDate)

        val (totalIncomeDecimal, totalOutcomeDecimal) = calculateIncomeAndOutcome(yearlyOperations)
        val balanceDecimal = totalIncomeDecimal - totalOutcomeDecimal

        return Triple(
            currencyCalculator.convertToFloatAndRound(totalIncomeDecimal),
            currencyCalculator.convertToFloatAndRound(totalOutcomeDecimal),
            currencyCalculator.convertToFloatAndRound(balanceDecimal)
        )
    }

    private suspend fun calculateIncomeAndOutcome(operations: List<Operation>): Pair<Long, Long> {
        var totalIncomeDecimal: Long = 0
        var totalOutcomeDecimal: Long = 0

        operations.forEach {
            val currencyPrice =
                currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

            val moneyDecimal = currencyCalculator.calculateMoneyAsDecimal(it.moneyAmount, currencyPrice)

            when (it.category.type) {
                CategoryType.INCOME -> totalIncomeDecimal += moneyDecimal
                CategoryType.OUTCOME -> totalOutcomeDecimal += moneyDecimal
            }
        }
        return Pair(totalIncomeDecimal, totalOutcomeDecimal)
    }
}