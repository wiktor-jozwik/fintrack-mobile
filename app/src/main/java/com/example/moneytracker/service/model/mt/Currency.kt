package com.example.moneytracker.service.model.mt

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
)