package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Currency
import java.time.LocalDateTime

class CurrencyRepository {
    private val moneyTrackerApi: IMoneyTrackerApi = MoneyTrackerApi()
    private val nbpApi: INbpApi = NbpApi()

    fun getAllCurrencies(): List<Currency> {
        return moneyTrackerApi.getAllCurrencies()
    }

    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDateTime): Double {
        return nbpApi.getPriceOfCurrencyAtDay(currency, date)
    }
}