package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.ApiResponse
import com.example.moneytracker.service.repository.internal.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun logoutUser(): Response<ApiResponse> {
        return userRepository.logoutUser()
    }
}