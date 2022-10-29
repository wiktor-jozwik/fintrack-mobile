package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.CurrencyRate
import com.example.moneytracker.service.model.mt.inputs.CurrencyCreateInput
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import java.time.LocalDate
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi
) {
    suspend fun getUsersCurrencies(): List<Currency> =
        responseErrorHandler(moneyTrackerApi.api.getUserCurrencies())

    suspend fun getCurrencyRates(
        baseCurrency: String,
        currency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CurrencyRate> =
        responseErrorHandler(moneyTrackerApi.api.getCurrencyRates(baseCurrency, currency, startDate, endDate))

    suspend fun getSupportedCurrencies(): List<Currency> =
        responseErrorHandler(moneyTrackerApi.api.getAllCurrencies())

    suspend fun getSupportedCurrenciesWithoutDefault(): List<Currency> =
        responseErrorHandler(moneyTrackerApi.api.getSupportedCurrenciesWithoutDefault())

    suspend fun getUserDefaultCurrency(): Currency =
        responseErrorHandler(moneyTrackerApi.api.getUserDefaultCurrency())

    suspend fun deleteUserCurrency(userCurrencyId: Int): Currency =
        responseErrorHandler(moneyTrackerApi.api.deleteUserCurrency(userCurrencyId))

    suspend fun addNewCurrency(currency: CurrencyCreateInput): Currency =
        responseErrorHandler(moneyTrackerApi.api.saveUserCurrency(currency))
}