package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    suspend fun getUsersCurrencies(): Response<List<Currency>> {
        return currencyRepository.getUsersCurrencies()
    }

    suspend fun deleteUserCurrency(userCurrencyId: Int): Response<Currency> {
        return currencyRepository.deleteUserCurrency(userCurrencyId)
    }
}