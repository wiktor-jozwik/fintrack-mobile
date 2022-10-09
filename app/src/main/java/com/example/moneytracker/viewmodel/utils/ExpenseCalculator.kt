package com.example.moneytracker.viewmodel.utils

import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.Expenses
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import javax.inject.Inject

class ExpenseCalculator @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val currencyCalculator: CurrencyCalculator
) {

    suspend fun calculate(operations: List<Operation>): Pair<Double, Double> {
        val defaultCurrencyName: String = currencyRepository.getUserDefaultCurrency().name

        val (totalIncome, totalOutcome) = calculateIncomesAndOutcomes(
            defaultCurrencyName,
            operations
        )
        return Pair(
            currencyCalculator.roundMoney(totalIncome),
            currencyCalculator.roundMoney(totalOutcome)
        )
    }


    private suspend fun calculateIncomesAndOutcomes(
        defaultCurrencyName: String,
        operations: List<Operation>
    ): Expenses {
        var incomes = 0.0
        var outcomes = 0.0

        operations.forEach {
            if (it.category.isInternal) {
                return@forEach
            }
            val moneyAmountInDefaultCurrency = if (it.currency.name == defaultCurrencyName) {
                it.moneyAmount
            } else {
                currencyRepository.convertCurrency(
                    it.currency.name,
                    defaultCurrencyName,
                    it.moneyAmount,
                    it.date
                )
            }

            when (it.category.type) {
                CategoryType.INCOME -> incomes += moneyAmountInDefaultCurrency
                CategoryType.OUTCOME -> outcomes += moneyAmountInDefaultCurrency
            }
        }

        return Expenses(incomes, outcomes)
    }
}