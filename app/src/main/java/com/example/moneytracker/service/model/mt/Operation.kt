package com.example.moneytracker.service.model.mt

import java.time.LocalDate

data class Operation(
    val id: Int,
    val name: String,
    val moneyAmount: Double,
    val category: Category,
    val date: LocalDate,
    val currency: Currency
)