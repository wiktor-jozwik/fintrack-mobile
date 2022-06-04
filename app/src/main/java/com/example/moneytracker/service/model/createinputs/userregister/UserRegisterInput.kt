package com.example.moneytracker.service.model.createinputs.userregister

import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterForm
import com.google.gson.annotations.SerializedName

data class UserRegisterInput(
    @SerializedName("user")
    val user: UserRegisterForm
)
