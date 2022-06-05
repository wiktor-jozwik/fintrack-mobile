package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.ApiResponse
import com.example.moneytracker.service.model.JwtResponse
import com.example.moneytracker.service.model.User
import com.example.moneytracker.service.model.createinputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterInput
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {

    suspend fun registerUser(userRegisterInput: UserRegisterInput): User {
        return moneyTrackerApi.api.registerUser(userRegisterInput)
    }

    suspend fun loginUser(userLoginInput: UserLoginInput): JwtResponse {
        return moneyTrackerApi.api.loginUser(userLoginInput)
    }

    suspend fun logoutUser(): ApiResponse {
        return moneyTrackerApi.api.logoutUser()
    }
}