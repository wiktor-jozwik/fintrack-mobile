package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Operation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("money_amount")
    val moneyAmount: Double,
    @SerializedName("category")
    val category: Category,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("currency")
    val currency: Currency
)