package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.service.repository.ft.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListCurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    suspend fun getUsersCurrencies(): List<Currency> =
        currencyRepository.getUsersCurrencies()

    suspend fun deleteUserCurrency(userCurrencyId: Int): Currency =
        currencyRepository.deleteUserCurrency(userCurrencyId)
}