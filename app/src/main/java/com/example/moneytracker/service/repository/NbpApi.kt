package com.example.moneytracker.service.repository

import java.time.LocalDateTime

class NbpApi : INbpApi {
    override fun getPriceOfCurrencyAtDay(currency: String, date: LocalDateTime): Double {
        val EURO_COST = 4.5516
        val DOLLAR_COST = 4.2324
        val PLN_COST = 1.0
        if (currency == "EUR") return EURO_COST
        if (currency == "USD") return DOLLAR_COST
        return PLN_COST
    }
}