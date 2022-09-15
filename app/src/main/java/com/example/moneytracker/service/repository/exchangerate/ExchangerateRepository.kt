package com.example.moneytracker.service.repository.exchangerate

import com.example.moneytracker.service.model.exchangerate.ExchangerateCurrencyRates
import javax.inject.Inject

class ExchangerateRepository @Inject constructor(
    private val exchangerateApi: ExchangerateApi
) {
    suspend fun getCurrencyRates(
        startDate: String,
        endDate: String,
        baseCurrency: String,
        chosenCurrency: String
    ): ExchangerateCurrencyRates {
        return exchangerateApi.api.getCurrencyRates(startDate, endDate, baseCurrency, chosenCurrency)
    }
}