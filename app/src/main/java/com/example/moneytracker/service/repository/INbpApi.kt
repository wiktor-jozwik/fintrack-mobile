package com.example.moneytracker.service.repository

import java.time.LocalDateTime

interface INbpApi {
    fun getPriceOfCurrencyAtDay(currency: String, date: LocalDateTime): Double
}