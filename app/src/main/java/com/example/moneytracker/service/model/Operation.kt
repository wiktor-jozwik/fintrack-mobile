package com.example.moneytracker.service.model

import java.time.LocalDateTime

data class Operation(
    val name: String,
    val moneyAmount: Double,
    val category: OperationCategory,
    val date: LocalDateTime
)