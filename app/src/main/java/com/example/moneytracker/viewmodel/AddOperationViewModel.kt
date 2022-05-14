package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import com.example.moneytracker.service.model.OperationCreateInput
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationCategoryRepository
import com.example.moneytracker.service.repository.OperationRepository
import kotlinx.coroutines.launch

class AddOperationViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()
    private val categoryRepository: OperationCategoryRepository = OperationCategoryRepository()
    private val currencyRepository: CurrencyRepository = CurrencyRepository()

    private val currenciesResponse: MutableLiveData<List<Currency>> = MutableLiveData()
    private val categoriesResponse: MutableLiveData<List<OperationCategory>> = MutableLiveData()
    private val operationSaveResponse: MutableLiveData<Operation> = MutableLiveData()

    fun addNewOperation(name: String, moneyAmount: Double, categoryName: String, currencyName: String): LiveData<Operation> {
        val operationCreateInput = OperationCreateInput(name, moneyAmount, categoryName, currencyName)

        viewModelScope.launch {
            val response = operationRepository.addNewOperation(operationCreateInput)
            operationSaveResponse.value = response
        }
        return operationSaveResponse
    }

    fun getAllCurrencies(): LiveData<List<Currency>> {
        viewModelScope.launch {
            val response = currencyRepository.getAllCurrencies()
            currenciesResponse.value = response
        }
        return currenciesResponse
    }

    fun getAllCategories(): LiveData<List<OperationCategory>> {
        viewModelScope.launch {
            val response = categoryRepository.getAllCategories()
            categoriesResponse.value = response
        }
        return categoriesResponse
    }
}