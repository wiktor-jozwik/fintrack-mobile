package com.example.fintrack.service.model.ft.inputs

import com.example.fintrack.service.model.ft.CategoryType

data class CategoryCreateInput(
    val name: String,
    val type: CategoryType,
    val isInternal: Boolean
)
