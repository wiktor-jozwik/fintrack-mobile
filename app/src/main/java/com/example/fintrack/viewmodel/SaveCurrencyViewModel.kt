package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.service.model.ft.inputs.CurrencyCreateInput
import com.example.fintrack.service.repository.ft.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveCurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    suspend fun addNewCurrency(name: String): Currency =
        currencyRepository.addNewCurrency(CurrencyCreateInput(name))

    suspend fun getSupportedCurrencies(): List<Currency> =
        currencyRepository.getSupportedCurrencies()
}