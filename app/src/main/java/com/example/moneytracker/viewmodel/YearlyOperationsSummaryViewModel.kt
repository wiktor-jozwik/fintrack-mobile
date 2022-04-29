package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.OperationCategoryType
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationRepository
import java.time.LocalDateTime
import kotlin.math.roundToInt

class YearlyOperationsSummaryViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()
    private val currencyRepository: CurrencyRepository = CurrencyRepository()
    private val MONEY_FACTOR = 100
    private val CURRENCY_FACTOR = 10000

    fun calculateYearlyIncomeAndOutcome(): Triple<Double, Double, Double> {
        val year = LocalDateTime.now().year
        val startDate = LocalDateTime.parse("${year}-01-01T00:00:01")
        val endDate = LocalDateTime.parse("${year}-12-31T23:59:59")

        val operationsYearly = operationRepository.getAllOperationsInRange(startDate, endDate)

        var totalIncome = 0.0
        var totalOutcome = 0.0

        operationsYearly.forEach {
            val currencyPrice = currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

            if (it.category.type == OperationCategoryType.INCOME) {
                totalIncome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            } else {
                totalOutcome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            }
        }
        val balance = totalIncome - totalOutcome

        return Triple(
            roundMoney(totalIncome),
            roundMoney(totalOutcome),
            roundMoney(balance)
        )
    }

    private fun roundMoney(money: Double): Double {
        return (money / (CURRENCY_FACTOR * MONEY_FACTOR) * 100.0).roundToInt() / 100.0
    }
}