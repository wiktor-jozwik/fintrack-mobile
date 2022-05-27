package com.example.moneytracker.service.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,
    val name: String,
    @SerializedName("operation_type")
    val type: CategoryType
)