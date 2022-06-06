package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.JwtResponse
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginForm
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginInput
import com.example.moneytracker.service.repository.internal.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Response<JwtResponse> {
        val userLoginInput = UserLoginInput(UserLoginForm(email, password))

        return userRepository.loginUser(userLoginInput)
    }
}