package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.Currency
import com.example.fintrack.service.model.mt.Operation
import com.example.fintrack.service.model.mt.inputs.OperationCreateInput
import com.example.fintrack.service.repository.mt.CategoryRepository
import com.example.fintrack.service.repository.mt.CurrencyRepository
import com.example.fintrack.service.repository.mt.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SaveOperationViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    suspend fun addNewOperation(
        name: String,
        moneyAmount: Double,
        date: LocalDate,
        categoryName: String,
        currencyName: String
    ): Operation {
        val operationCreateInput =
            OperationCreateInput(
                name,
                moneyAmount,
                date.toString(),
                categoryName,
                currencyName
            )

        return operationRepository.addNewOperation(operationCreateInput)
    }

    suspend fun editOperation(
        id: Int,
        name: String,
        moneyAmount: Double,
        date: LocalDate,
        categoryName: String,
        currencyName: String
    ): Operation {
        val operationCreateInput =
            OperationCreateInput(
                name,
                moneyAmount,
                date.toString(),
                categoryName,
                currencyName
            )

        return operationRepository.editOperation(id, operationCreateInput)
    }

    suspend fun getUsersCurrencies(): List<Currency> =
        currencyRepository.getUsersCurrencies()

    suspend fun getAllCategories(): List<Category> =
        categoryRepository.getAllCategories()
}