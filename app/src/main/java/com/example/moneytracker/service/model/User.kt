package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user")
    val email: String,
)
