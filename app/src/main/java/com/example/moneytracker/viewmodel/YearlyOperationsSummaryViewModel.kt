package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategoryType
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class YearlyOperationsSummaryViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository
): ViewModel() {
    private val MONEY_FACTOR = 100
    private val CURRENCY_FACTOR = 10000
    private val yearlyCalculationResponse: MutableLiveData<Triple<Double, Double, Double>> = MutableLiveData()

    suspend fun calculateYearlyIncomeAndOutcome(): MutableLiveData<Triple<Double, Double, Double>> {
        val year = LocalDate.now().year
        val startDate = LocalDate.parse("${year}-01-01")
        val endDate = LocalDate.parse("${year}-12-31")
        val yearlyOperations = operationRepository.getAllOperationsInRanges(startDate, endDate)

        val (totalIncome, totalOutcome) = calculateIncomeAndOutcome(yearlyOperations)
        val balance = totalIncome - totalOutcome

        yearlyCalculationResponse.value = Triple(
            roundMoney(totalIncome),
            roundMoney(totalOutcome),
            roundMoney(balance)
        )
        return yearlyCalculationResponse
    }

    private fun calculateIncomeAndOutcome(operations: List<Operation>): Pair<Double, Double> {
        var totalIncome = 0.0
        var totalOutcome = 0.0

        operations.forEach {
            val currencyPrice = currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

            if (it.category.type == OperationCategoryType.INCOME) {
                totalIncome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            } else if (it.category.type == OperationCategoryType.OUTCOME){
                totalOutcome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            }
        }
        return Pair(totalIncome, totalOutcome)
    }

    private fun roundMoney(money: Double): Double {
        return (money / (CURRENCY_FACTOR * MONEY_FACTOR) * 100.0).roundToInt() / 100.0
    }
}