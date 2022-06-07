package com.example.moneytracker.service.model.mt.inputs.userlogin

import com.google.gson.annotations.SerializedName

data class UserLoginInput(
    @SerializedName("user")
    val user: UserLoginForm
)
