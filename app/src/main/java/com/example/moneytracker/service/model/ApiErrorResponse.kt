package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)
