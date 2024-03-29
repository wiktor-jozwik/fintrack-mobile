package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.JwtResponse
import com.example.fintrack.service.model.ft.UserProfileData
import com.example.fintrack.service.model.ft.inputs.UserLoginInput
import com.example.fintrack.service.repository.ft.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): JwtResponse =
        userRepository.loginUser(UserLoginInput(email, password))

    suspend fun getProfileData(): UserProfileData =
        userRepository.getProfileData()

}