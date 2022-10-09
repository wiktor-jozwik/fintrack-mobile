package com.example.moneytracker.service.model.mt.inputs

import com.example.moneytracker.service.model.mt.CategoryType

data class CategoryCreateInput(
    val name: String,
    val type: CategoryType,
    val isInternal: Boolean
)
