package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.ApiResponse
import com.example.moneytracker.service.repository.internal.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val userLogoutResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    suspend fun logoutUser(): MutableLiveData<ApiResponse> {
        userLogoutResponse.value = userRepository.logoutUser()

        return userLogoutResponse
    }
}