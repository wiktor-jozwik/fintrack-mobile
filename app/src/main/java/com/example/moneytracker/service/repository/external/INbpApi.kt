package com.example.moneytracker.service.repository.external

import java.time.LocalDate

interface INbpApi {
    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDate): Double
}