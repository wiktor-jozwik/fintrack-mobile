package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.repository.external.INbpApi
import com.example.moneytracker.service.repository.external.NbpApi
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    private val nbpApi: INbpApi = NbpApi()

    suspend fun getAllCurrencies(): Response<List<Currency>> =
        moneyTrackerApi.api.getAllCurrencies()

    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDate): Double =
        nbpApi.getPriceOfCurrencyAtDay(currency, date)
}