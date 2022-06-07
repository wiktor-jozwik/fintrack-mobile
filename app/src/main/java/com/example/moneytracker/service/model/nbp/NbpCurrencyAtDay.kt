package com.example.moneytracker.service.model.nbp

data class NpbCurrencyAtDay(
    val code: String,
    val currency: String,
    val rates: List<NbpRate>,
    val table: String
)