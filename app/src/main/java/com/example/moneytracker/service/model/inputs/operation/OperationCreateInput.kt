package com.example.moneytracker.service.model.inputs.operation

import com.example.moneytracker.service.model.inputs.userlogin.UserLoginForm
import com.google.gson.annotations.SerializedName

data class OperationCreateInput(
    @SerializedName("operation")
    val operation: OperationCreateForm
)
