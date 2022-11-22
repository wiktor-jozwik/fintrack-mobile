package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.JwtResponse
import com.example.fintrack.service.model.mt.inputs.UserLoginInput
import com.example.fintrack.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): JwtResponse =
        userRepository.loginUser(UserLoginInput(email, password))
}