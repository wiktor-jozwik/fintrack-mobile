package com.example.moneytracker.service.model.exchangerate

data class ExchangerateCurrencyRates (
    val rates: Map<String, Map<String, Double>>
)
