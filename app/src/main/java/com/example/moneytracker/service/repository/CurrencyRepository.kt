package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Currency
import java.time.LocalDateTime

class CurrencyRepository {
    private val nbpApi: INbpApi = NbpApi()

    suspend fun getAllCurrencies(): List<Currency> {
        return MoneyTrackerApi.api.getAllCurrencies()
    }

    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDateTime): Double {
        return nbpApi.getPriceOfCurrencyAtDay(currency, date)
    }
}