package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import java.time.LocalDate
import java.time.LocalDateTime

interface IMoneyTrackerApi {
    fun getAllOperations(): List<Operation>

    fun getAllOperationsInRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Operation>

    fun getAllCategories(): List<OperationCategory>

    fun getAllCurrencies(): List<Currency>

    fun saveOperation(name: String, moneyAmount: Double, category: String, currency: String)

    fun saveCategory(category: OperationCategory)
}