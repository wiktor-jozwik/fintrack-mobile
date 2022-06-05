package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("response")
    val response: String
)