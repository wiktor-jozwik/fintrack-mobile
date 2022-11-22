package com.example.fintrack.service.model.mt

data class JwtResponse(
    val jwtAccessToken: String,
    val jwtRefreshToken: String,
)