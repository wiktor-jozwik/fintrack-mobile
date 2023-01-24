package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.service.model.ft.User
import com.example.fintrack.service.model.ft.inputs.UserRegisterInput
import com.example.fintrack.service.repository.ft.CurrencyRepository
import com.example.fintrack.service.repository.ft.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    suspend fun getSupportedCurrencies(): List<Currency> =
        currencyRepository.getSupportedCurrencies()

    suspend fun registerUser(
        email: String,
        password: String,
        passwordConfirmation: String,
        defaultCurrencyName: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?
    ): User {
        val userRegisterInput =
            UserRegisterInput(
                email,
                password,
                passwordConfirmation,
                defaultCurrencyName,
                firstName,
                lastName,
                phoneNumber
            )

        return userRepository.registerUser(userRegisterInput)
    }
}