package com.example.moneytracker.service.model.mt

data class User(
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
)
