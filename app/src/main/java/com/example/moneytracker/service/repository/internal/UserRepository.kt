package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.ApiResponse
import com.example.moneytracker.service.model.JwtResponse
import com.example.moneytracker.service.model.User
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.inputs.userregister.UserRegisterInput
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun registerUser(userRegisterInput: UserRegisterInput): User =
        moneyTrackerApi.api.registerUser(userRegisterInput)

    suspend fun loginUser(userLoginInput: UserLoginInput): JwtResponse =
        moneyTrackerApi.api.loginUser(userLoginInput)

    suspend fun logoutUser(): ApiResponse =
        moneyTrackerApi.api.logoutUser()
}