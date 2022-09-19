package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.inputs.CurrencyCreateInput
import com.example.moneytracker.service.repository.exchangerate.ExchangerateApi
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import java.time.LocalDate
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
    private val exchangerateApi: ExchangerateApi
) {
    suspend fun getUsersCurrencies(): List<Currency> =
        responseErrorHandler(moneyTrackerApi.api.getUserCurrencies())

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

    suspend fun convertCurrency(
        from: String,
        to: String,
        moneyAmount: Double,
        date: LocalDate
    ): Double {
        val convertedValue =
            exchangerateApi.api.convertCurrency(
                from,
                to,
                date.formatToIsoDateWithDashes(),
                moneyAmount.toString()
            )

        return convertedValue.result
    }
}