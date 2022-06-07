package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.User
import com.example.moneytracker.service.model.mt.inputs.userregister.UserRegisterForm
import com.example.moneytracker.service.model.mt.inputs.userregister.UserRegisterInput
import com.example.moneytracker.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun registerUser(
        email: String,
        password: String,
        passwordConfirmation: String
    ): Response<User> {
        val userRegisterInput =
            UserRegisterInput(UserRegisterForm(email, password, passwordConfirmation))

        return userRepository.registerUser(userRegisterInput)
    }
}