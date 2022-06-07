package com.example.moneytracker.service.model.mt.inputs.userregister

import com.google.gson.annotations.SerializedName

data class UserRegisterInput(
    @SerializedName("user")
    val user: UserRegisterForm
)
