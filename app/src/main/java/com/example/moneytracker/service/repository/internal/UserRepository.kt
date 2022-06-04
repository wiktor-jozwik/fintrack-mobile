package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.JwtResponse
import com.example.moneytracker.service.model.User
import com.example.moneytracker.service.model.createinputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterInput

class UserRepository {
    suspend fun registerUser(userRegisterInput: UserRegisterInput): User {
        return MoneyTrackerApi.api.registerUser(userRegisterInput)
    }

    suspend fun loginUser(userLoginInput: UserLoginInput): JwtResponse {
        return MoneyTrackerApi.api.loginUser(userLoginInput)
    }

    suspend fun logoutUser() {
        MoneyTrackerApi.api.logoutUser()
    }
}