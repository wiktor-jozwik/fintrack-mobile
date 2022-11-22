package com.example.fintrack.service.model.mt

data class Category(
    val id: Int,
    val name: String,
    val type: CategoryType,
    val isInternal: Boolean
)