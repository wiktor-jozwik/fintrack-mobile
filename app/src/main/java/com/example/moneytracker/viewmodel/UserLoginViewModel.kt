package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.JwtResponse
import com.example.moneytracker.service.model.mt.inputs.UserLoginInput
import com.example.moneytracker.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Response<JwtResponse> {
        val userLoginInput = UserLoginInput(email, password)

        return userRepository.loginUser(userLoginInput)
    }
}