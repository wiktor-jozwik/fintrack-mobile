package com.example.moneytracker.service.model.createinputs

import com.example.moneytracker.service.model.CategoryType
import com.google.gson.annotations.SerializedName

data class CategoryCreateInput(
    val name: String,
    @SerializedName("operation_type")
    val type: CategoryType
)