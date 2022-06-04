package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.repository.external.INbpApi
import com.example.moneytracker.service.repository.external.NbpApi
import java.time.LocalDate

class CurrencyRepository {
    private val nbpApi: INbpApi = NbpApi()

    suspend fun getAllCurrencies(): List<Currency> {
        return MoneyTrackerApi.api.getAllCurrencies()
    }

    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDate): Double {
        return nbpApi.getPriceOfCurrencyAtDay(currency, date)
    }
}