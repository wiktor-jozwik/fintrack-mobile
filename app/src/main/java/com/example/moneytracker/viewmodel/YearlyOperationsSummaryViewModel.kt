package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytracker.service.model.OperationCategoryType
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

class YearlyOperationsSummaryViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()
    private val currencyRepository: CurrencyRepository = CurrencyRepository()
    private val MONEY_FACTOR = 100
    private val CURRENCY_FACTOR = 10000
    private val yearlyCalculationResponse: MutableLiveData<Triple<Double, Double, Double>> = MutableLiveData()

    fun calculateYearlyIncomeAndOutcome(): LiveData<Triple<Double, Double, Double>> {
        viewModelScope.launch {
            val year = LocalDate.now().year
            val startDate = LocalDate.parse("${year}-01-01")
            val endDate = LocalDate.parse("${year}-12-31")
            val yearlyOperations = operationRepository.getAllOperationsInRanges(startDate, endDate)

            var totalIncome = 0.0
            var totalOutcome = 0.0

            yearlyOperations.forEach {
                val currencyPrice = currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

                if (it.category.type == OperationCategoryType.INCOME) {
                    totalIncome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
                } else if (it.category.type == OperationCategoryType.OUTCOME){
                    totalOutcome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
                }
            }
            val balance = totalIncome - totalOutcome

            yearlyCalculationResponse.value = Triple(
                roundMoney(totalIncome),
                roundMoney(totalOutcome),
                roundMoney(balance)
            )
        }
        return yearlyCalculationResponse
    }

    private fun roundMoney(money: Double): Double {
        return (money / (CURRENCY_FACTOR * MONEY_FACTOR) * 100.0).roundToInt() / 100.0
    }
}