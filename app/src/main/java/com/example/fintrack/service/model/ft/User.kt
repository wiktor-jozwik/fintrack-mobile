package com.example.fintrack.service.model.ft

data class User(
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
)
