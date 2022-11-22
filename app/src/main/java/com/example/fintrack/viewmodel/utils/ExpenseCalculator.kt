package com.example.fintrack.viewmodel.utils

import com.example.fintrack.service.model.mt.CategoryType
import com.example.fintrack.service.model.mt.DefaultCurrencyOperation
import com.example.fintrack.service.model.mt.Expenses
import javax.inject.Inject

class ExpenseCalculator @Inject constructor(
    private val currencyCalculator: CurrencyCalculator
) {

    fun calculate(operations: List<DefaultCurrencyOperation>): Pair<Double, Double> {
        val (totalIncome, totalOutcome) = calculateIncomesAndOutcomes(
            operations
        )
        return Pair(
            currencyCalculator.roundMoney(totalIncome),
            currencyCalculator.roundMoney(totalOutcome)
        )
    }


    private fun calculateIncomesAndOutcomes(
        operations: List<DefaultCurrencyOperation>
    ): Expenses {
        var incomes = 0.0
        var outcomes = 0.0

        operations.forEach {
            if (it.category.isInternal) {
                return@forEach
            }

            when (it.category.type) {
                CategoryType.INCOME -> incomes += it.moneyAmountInDefaultCurrency
                CategoryType.OUTCOME -> outcomes += it.moneyAmountInDefaultCurrency
            }
        }

        return Expenses(incomes, outcomes)
    }
}