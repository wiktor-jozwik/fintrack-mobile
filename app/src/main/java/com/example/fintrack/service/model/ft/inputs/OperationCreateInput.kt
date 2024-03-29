package com.example.fintrack.service.model.ft.inputs

data class OperationCreateInput(
    val name: String,
    val moneyAmount: Double,
    val date: String,
    val categoryName: String,
    val currencyName: String
)
