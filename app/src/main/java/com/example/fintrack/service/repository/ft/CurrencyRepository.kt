package com.example.fintrack.service.repository.ft

import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.service.model.ft.CurrencyRate
import com.example.fintrack.service.model.ft.inputs.CurrencyCreateInput
import com.example.fintrack.view.ui.utils.responseErrorHandler
import java.time.LocalDate
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val finTrackApi: FinTrackApi
) {
    suspend fun getUsersCurrencies(): List<Currency> =
        responseErrorHandler(finTrackApi.api.getUserCurrencies())

    suspend fun getCurrencyRates(
        baseCurrency: String,
        currency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CurrencyRate> =
        responseErrorHandler(finTrackApi.api.getCurrencyRates(baseCurrency, currency, startDate, endDate))

    suspend fun getSupportedCurrencies(): List<Currency> =
        responseErrorHandler(finTrackApi.api.getAllCurrencies())

    suspend fun deleteUserCurrency(userCurrencyId: Int): Currency =
        responseErrorHandler(finTrackApi.api.deleteUserCurrency(userCurrencyId))

    suspend fun addNewCurrency(currency: CurrencyCreateInput): Currency =
        responseErrorHandler(finTrackApi.api.saveUserCurrency(currency))
}