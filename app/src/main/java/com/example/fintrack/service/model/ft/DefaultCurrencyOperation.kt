package com.example.fintrack.service.model.ft

import java.time.LocalDate

data class DefaultCurrencyOperation(
    val name: String,
    val moneyAmountInDefaultCurrency: Double,
    val date: LocalDate,
    val category: Category,
)