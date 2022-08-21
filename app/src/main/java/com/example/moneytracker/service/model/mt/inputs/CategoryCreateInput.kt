package com.example.moneytracker.service.model.mt.inputs

import com.example.moneytracker.service.model.mt.CategoryType
import com.google.gson.annotations.SerializedName

data class CategoryCreateInput(
    val name: String,
    val type: CategoryType
)
