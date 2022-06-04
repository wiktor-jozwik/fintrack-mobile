package com.example.moneytracker.service.model.createinputs.userregister

import com.google.gson.annotations.SerializedName

data class UserRegisterForm(
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)
