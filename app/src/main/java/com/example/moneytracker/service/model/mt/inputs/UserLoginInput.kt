package com.example.moneytracker.service.model.mt.inputs

import com.google.gson.annotations.SerializedName

data class UserLoginInput(
    val email: String,
    val password: String
)
