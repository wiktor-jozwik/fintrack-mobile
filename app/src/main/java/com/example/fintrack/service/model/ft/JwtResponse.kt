package com.example.fintrack.service.model.ft

data class JwtResponse(
    val jwtAccessToken: String,
    val jwtRefreshToken: String,
)