package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.repository.mt.CurrencyRepository
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