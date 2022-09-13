package com.example.moneytracker.service.model.mt.inputs

data class UserRegisterInput(
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val defaultCurrencyName: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
)
