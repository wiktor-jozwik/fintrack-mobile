package com.example.moneytracker.service.model.createinputs

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