package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.Currency
import com.example.fintrack.service.model.mt.inputs.CurrencyCreateInput
import com.example.fintrack.service.repository.mt.CurrencyRepository
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