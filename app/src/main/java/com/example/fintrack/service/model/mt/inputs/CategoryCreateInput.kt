package com.example.fintrack.service.model.mt.inputs

import com.example.fintrack.service.model.mt.CategoryType

data class CategoryCreateInput(
    val name: String,
    val type: CategoryType,
    val isInternal: Boolean
)
