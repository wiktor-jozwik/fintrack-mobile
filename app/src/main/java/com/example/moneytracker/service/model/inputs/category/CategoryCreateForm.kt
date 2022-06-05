package com.example.moneytracker.service.model.inputs.category

import com.example.moneytracker.service.model.CategoryType
import com.google.gson.annotations.SerializedName

data class CategoryCreateForm(
    @SerializedName("name")
    val name: String,
    @SerializedName("operation_type")
    val type: CategoryType
)
