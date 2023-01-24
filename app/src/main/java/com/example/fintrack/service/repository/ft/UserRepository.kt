package com.example.fintrack.service.repository.ft

import com.example.fintrack.service.model.ft.*
import com.example.fintrack.service.model.ft.inputs.ResendEmailConfirmationInput
import com.example.fintrack.service.model.ft.inputs.UserLoginInput
import com.example.fintrack.service.model.ft.inputs.UserRegisterInput
import com.example.fintrack.view.ui.utils.responseErrorHandler
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val finTrackApi: FinTrackApi,
) {
    suspend fun registerUser(userRegisterInput: UserRegisterInput): User =
        responseErrorHandler(finTrackApi.api.registerUser(userRegisterInput))

    suspend fun loginUser(userLoginInput: UserLoginInput): JwtResponse =
        responseErrorHandler(finTrackApi.api.loginUser(userLoginInput))

    suspend fun logoutUser(): LogoutResponse =
        responseErrorHandler(finTrackApi.api.logoutUser())

    suspend fun resendEmail(resendEmailConfirmationInput: ResendEmailConfirmationInput): StringResponse =
        responseErrorHandler(finTrackApi.api.resendEmail(resendEmailConfirmationInput))

    suspend fun getProfileData(): UserProfileData =
        responseErrorHandler(finTrackApi.api.getProfileData())
}