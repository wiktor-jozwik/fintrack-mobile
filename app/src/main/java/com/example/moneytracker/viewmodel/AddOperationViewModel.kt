package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.model.mt.inputs.OperationCreateInput
import com.example.moneytracker.service.repository.mt.CategoryRepository
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.service.repository.mt.OperationRepository
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
                name,
                moneyAmount,
                date.toString(),
                categoryName,
                currencyName
            )

        return operationRepository.addNewOperation(operationCreateInput)
    }

    suspend fun getUsersCurrencies(): Response<List<Currency>> {
        return currencyRepository.getUsersCurrencies()
    }

    suspend fun getAllCategories(): Response<List<Category>> {
        return categoryRepository.getAllCategories()
    }
}