package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.model.mt.User
import com.example.moneytracker.service.model.mt.inputs.UserRegisterInput
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
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
    ): Response<User> {
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