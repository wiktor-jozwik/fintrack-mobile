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
    suspend fun registerUser(userRegisterInput: UserRegisterInput): Response<User> =
        moneyTrackerApi.api.registerUser(userRegisterInput)

    suspend fun loginUser(userLoginInput: UserLoginInput): Response<JwtResponse> =
        moneyTrackerApi.api.loginUser(userLoginInput)

    suspend fun logoutUser(): LogoutResponse =
        responseErrorHandler(moneyTrackerApi.api.logoutUser())

    suspend fun resendEmail(resendEmailConfirmationInput: ResendEmailConfirmationInput): Response<StringResponse> =
        moneyTrackerApi.api.resendEmail(resendEmailConfirmationInput)

    suspend fun getProfileData(): Response<UserProfileData> =
        moneyTrackerApi.api.getProfileData()
}