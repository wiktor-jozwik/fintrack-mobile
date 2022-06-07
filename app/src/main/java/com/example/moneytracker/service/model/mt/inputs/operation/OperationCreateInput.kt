package com.example.moneytracker.service.model.mt.inputs.operation

import com.google.gson.annotations.SerializedName

data class OperationCreateInput(
    @SerializedName("operation")
    val operation: OperationCreateForm
)
