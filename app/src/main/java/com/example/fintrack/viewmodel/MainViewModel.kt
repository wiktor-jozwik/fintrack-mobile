package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.LogoutResponse
import com.example.fintrack.service.repository.ft.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun logout(): LogoutResponse =
        userRepository.logoutUser()
}