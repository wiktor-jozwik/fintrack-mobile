package com.example.moneytracker.service.model.mt

import com.google.gson.annotations.SerializedName

data class JwtResponse(
//    @SerializedName("success")
//    val success: Boolean,
    val jwtToken: String,
//    @SerializedName("response")
//    val response: String
)