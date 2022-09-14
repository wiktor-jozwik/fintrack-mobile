package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.inputs.CategoryCreateInput
import com.example.moneytracker.service.model.mt.inputs.CurrencyCreateInput
import com.example.moneytracker.service.repository.exchangerate.ExchangerateApi
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
    private val exchangerateApi: ExchangerateApi
) {
    suspend fun getUsersCurrencies(): Response<List<Currency>> =
        moneyTrackerApi.api.getUsersCurrencies()

    suspend fun getSupportedCurrencies(): Response<List<Currency>> =
        moneyTrackerApi.api.getAllCurrencies()

    suspend fun getUserDefaultCurrency(): Response<Currency> =
        moneyTrackerApi.api.getUserDefaultCurrency()

    suspend fun addNewCurrency(currency: CurrencyCreateInput): Response<Currency> =
        moneyTrackerApi.api.saveCurrency(currency)

    suspend fun convertCurrency(from: String, to: String, moneyAmount: Double, date: LocalDate): Double {
        val convertedValue =
            exchangerateApi.api.convertCurrency(from, to, date.formatToIsoDateWithDashes(), moneyAmount.toString())

        return convertedValue.result
    }
}