package com.example.fintrack.service.model.nbp

import com.google.gson.annotations.SerializedName

data class NbpGoldPrice(
    @SerializedName("cena")
    val price: Double,
    @SerializedName("data")
    val date: String
)
