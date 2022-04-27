package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.CurrencyEnum
import com.example.moneytracker.service.repository.OperationRepository
import java.time.LocalDateTime
import kotlin.math.roundToInt

class YearlyOperationsSummaryViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()
    private val EURO_COST = 4.5516
    private val DOLLAR_COST = 4.2324
    private val MONEY_FACTOR = 100
    private val CURRENCY_FACTOR = 10000

    fun calculateYearlyIncomeAndOutcome(): Triple<Double, Double, Double> {
        val year = LocalDateTime.now().year
        val startDate = LocalDateTime.parse("${year}-01-01T00:00:01")
        val endDate = LocalDateTime.parse("${year}-12-31T23:59:59")

        val operationsYearly = operationRepository.getOperationListInRange(startDate, endDate)

        var totalIncome = 0.0
        var totalOutcome = 0.0

        operationsYearly.forEach {
            var currencyPrice = 1.0
            if (it.currency == CurrencyEnum.EUR) currencyPrice = EURO_COST
            if (it.currency == CurrencyEnum.USD) currencyPrice = DOLLAR_COST

            if (it.moneyAmount > 0) {
                totalIncome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            } else {
                totalOutcome += it.moneyAmount * CURRENCY_FACTOR * currencyPrice * MONEY_FACTOR
            }
        }
        val balance = totalIncome + totalOutcome

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