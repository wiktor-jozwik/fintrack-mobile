package com.example.moneytracker.service.model.inputs.userlogin

import com.google.gson.annotations.SerializedName

data class UserLoginForm(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
