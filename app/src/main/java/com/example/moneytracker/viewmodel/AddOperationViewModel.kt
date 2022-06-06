package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.inputs.operation.OperationCreateForm
import com.example.moneytracker.service.model.inputs.operation.OperationCreateInput
import com.example.moneytracker.service.repository.internal.CategoryRepository
import com.example.moneytracker.service.repository.internal.CurrencyRepository
import com.example.moneytracker.service.repository.internal.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddOperationViewModel @Inject constructor(
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
    ): Response<Operation> {
        val operationCreateInput =
            OperationCreateInput(
                OperationCreateForm(
                    name,
                    moneyAmount,
                    date.toString(),
                    categoryName,
                    currencyName
                )
            )

        return operationRepository.addNewOperation(operationCreateInput)
    }

    suspend fun getAllCurrencies(): Response<List<Currency>> {
        return currencyRepository.getAllCurrencies()
    }

    suspend fun getAllCategories(): Response<List<Category>> {
        return categoryRepository.getAllCategories()
    }
}