package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.*
import com.example.moneytracker.service.model.mt.inputs.ResendEmailConfirmationInput
import com.example.moneytracker.service.model.mt.inputs.UserLoginInput
import com.example.moneytracker.service.model.mt.inputs.UserRegisterInput
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun registerUser(userRegisterInput: UserRegisterInput): User =
        responseErrorHandler(moneyTrackerApi.api.registerUser(userRegisterInput))

    suspend fun loginUser(userLoginInput: UserLoginInput): JwtResponse =
        responseErrorHandler(moneyTrackerApi.api.loginUser(userLoginInput))

    suspend fun logoutUser(): LogoutResponse =
        responseErrorHandler(moneyTrackerApi.api.logoutUser())

    suspend fun resendEmail(resendEmailConfirmationInput: ResendEmailConfirmationInput): StringResponse =
        responseErrorHandler(moneyTrackerApi.api.resendEmail(resendEmailConfirmationInput))

    suspend fun getProfileData(): UserProfileData =
        responseErrorHandler(moneyTrackerApi.api.getProfileData())
}