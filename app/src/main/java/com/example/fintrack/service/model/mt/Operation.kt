package com.example.fintrack.service.model.mt

import java.time.LocalDate

data class Operation(
    val id: Int,
    val name: String,
    val moneyAmount: Double,
    val category: Category,
    val date: LocalDate,
    val currency: Currency
)