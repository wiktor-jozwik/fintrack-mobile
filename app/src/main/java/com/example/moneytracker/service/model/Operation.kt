package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Operation(
    val id: Int,
    val name: String,
    @SerializedName("money_amount")
    val moneyAmount: Double,
    val category: OperationCategory,
    @SerializedName("created_at")
    val date: LocalDateTime,
    val currency: Currency
)