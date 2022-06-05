package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.JwtResponse
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginForm
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginInput
import com.example.moneytracker.service.repository.internal.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val userLoginResponse: MutableLiveData<JwtResponse> = MutableLiveData()

    suspend fun loginUser(email: String, password: String): MutableLiveData<JwtResponse> {
        val userLoginInput = UserLoginInput(UserLoginForm(email, password))

        userLoginResponse.value = userRepository.loginUser(userLoginInput)

        return userLoginResponse
    }
}