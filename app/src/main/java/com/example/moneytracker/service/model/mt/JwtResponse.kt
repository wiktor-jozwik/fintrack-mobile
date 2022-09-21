package com.example.moneytracker.service.model.mt

data class JwtResponse(
    val jwtAccessToken: String,
    val jwtRefreshToken: String,
)