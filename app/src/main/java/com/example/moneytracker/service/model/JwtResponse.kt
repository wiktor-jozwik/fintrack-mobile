package com.example.moneytracker.service.model

data class JwtResponse (
    val success: String,
    val jwt: String,
    val response: String
)