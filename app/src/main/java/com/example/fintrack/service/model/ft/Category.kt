package com.example.fintrack.service.model.ft

data class Category(
    val id: Int,
    val name: String,
    val type: CategoryType,
    val isInternal: Boolean
)