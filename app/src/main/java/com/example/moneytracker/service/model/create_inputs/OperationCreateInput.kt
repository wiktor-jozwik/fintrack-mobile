package com.example.moneytracker.service.model.create_inputs

import com.google.gson.annotations.SerializedName

data class OperationCreateInput(
    @SerializedName("name")
    val name: String,
    @SerializedName("money_amount")
    val moneyAmount: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("currency_name")
    val currencyName: String
)