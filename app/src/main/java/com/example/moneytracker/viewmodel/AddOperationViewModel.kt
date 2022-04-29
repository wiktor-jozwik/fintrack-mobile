package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationCategoryRepository
import com.example.moneytracker.service.repository.OperationRepository

class AddOperationViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()
    private val categoryRepository: OperationCategoryRepository = OperationCategoryRepository()
    private val currencyRepository: CurrencyRepository = CurrencyRepository()

    fun addNewOperation(name: String, moneyAmount: Double, category: String, currency: String) {


//        val operation: Operation = Operation(name, moneyAmount, category, date, currency)
        operationRepository.addNewOperation(name, moneyAmount, category, currency)
    }

    fun getAllCurrencies(): List<Currency> {
        return currencyRepository.getAllCurrencies()
    }

    fun getAllCategories(): List<OperationCategory> {
        return categoryRepository.getAllCategories()
    }
}