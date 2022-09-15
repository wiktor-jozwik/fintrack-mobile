package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.inputs.CurrencyCreateInput
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddCurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    suspend fun addNewCurrency(name: String): Response<Currency> {
        val categoryCreateInput = CurrencyCreateInput(name)

        return currencyRepository.addNewCurrency(categoryCreateInput)
    }

    suspend fun getSupportedCurrencies(): Response<List<Currency>> {
        return currencyRepository.getSupportedCurrencies()
    }
}