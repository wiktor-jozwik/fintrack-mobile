package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.ApiResponse
import com.example.moneytracker.service.model.mt.JwtResponse
import com.example.moneytracker.service.model.mt.User
import com.example.moneytracker.service.model.mt.inputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.mt.inputs.userregister.UserRegisterInput
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun registerUser(userRegisterInput: UserRegisterInput): Response<User> =
        moneyTrackerApi.api.registerUser(userRegisterInput)

    suspend fun loginUser(userLoginInput: UserLoginInput): Response<JwtResponse> =
        moneyTrackerApi.api.loginUser(userLoginInput)

    suspend fun logoutUser(): Response<ApiResponse> =
        moneyTrackerApi.api.logoutUser()
}