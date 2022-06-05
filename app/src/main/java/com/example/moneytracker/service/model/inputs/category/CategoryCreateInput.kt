package com.example.moneytracker.service.model.inputs.category

import com.google.gson.annotations.SerializedName

data class CategoryCreateInput(
    @SerializedName("category")
    val category: CategoryCreateForm
)