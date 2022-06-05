package com.example.moneytracker.service.model.inputs.userregister

import com.google.gson.annotations.SerializedName

data class UserRegisterForm(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)
