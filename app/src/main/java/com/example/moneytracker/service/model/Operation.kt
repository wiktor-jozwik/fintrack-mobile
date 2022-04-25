package com.example.moneytracker.service.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Operation(
    var name: String,
    var moneyAmount: Double,
    var category: OperationCategory,
    var date: LocalDateTime
)