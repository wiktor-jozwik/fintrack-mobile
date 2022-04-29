package com.example.moneytracker.service.repository

import android.util.Log
import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import com.example.moneytracker.service.model.OperationCategoryType
import java.time.LocalDate
import java.time.LocalDateTime

private var categories: MutableList<OperationCategory> = mutableListOf(
    OperationCategory("Income category", OperationCategoryType.INCOME),
    OperationCategory("Outcome category", OperationCategoryType.OUTCOME)
)
private var currencies: MutableList<Currency> = mutableListOf(
    Currency("PLN", "zł"),
    Currency("EUR", "€"),
    Currency("USD", "$"),
)

private var operations: MutableList<Operation> = mutableListOf(
    Operation("Przelew", 10.55, categories[1], LocalDateTime.parse("2022-01-06T21:30:10"), currencies[0]),
    Operation("Przelew", 10.55, categories[1], LocalDateTime.parse("2022-01-06T21:30:10"), currencies[0]),
    Operation("Przelew2", 50.56, categories[1], LocalDateTime.parse("2022-03-06T14:30:55"), currencies[1]),
    Operation("Przelew3", 25.66, categories[0], LocalDateTime.parse("2021-12-06T16:30:33")),
    Operation("Przelew2", 68.29, categories[0], LocalDateTime.parse("2022-04-06T14:30:55")),
    Operation("Przelew2", 55.29, categories[0], LocalDateTime.parse("2022-02-04T14:30:55")),
    Operation("Przelew2", 72.29, categories[0], LocalDateTime.parse("2019-04-06T14:30:55"))
)

class MoneyTrackerApi: IMoneyTrackerApi {
    override fun getAllOperations(): List<Operation> {
        return operations
    }

    override fun getAllOperationsInRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Operation> {
        val ops = getAllOperations()

        return ops.filter {
            it.date > startDate && it.date <= endDate
        }
    }

    override fun getAllCategories(): List<OperationCategory> {
        return categories
    }

    override fun getAllCurrencies(): List<Currency> {
        return currencies
    }

    override fun saveCategory(category: OperationCategory) {
        categories.add(category)
    }

    override fun saveOperation(name: String, moneyAmount: Double, category: String, currency: String) {
        // send request to api with parameter above
        // for now mocked operations:

        val categoryObj: OperationCategory = categories.first { it.name == category }
        val currencyObj: Currency = currencies.first { it.name == currency }
        val operation = Operation(name, moneyAmount, categoryObj, LocalDateTime.now(), currencyObj)
        operations.add(operation)
    }
}