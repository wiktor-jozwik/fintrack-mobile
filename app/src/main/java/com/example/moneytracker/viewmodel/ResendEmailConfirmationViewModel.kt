package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.StringResponse
import com.example.moneytracker.service.model.mt.inputs.ResendEmailConfirmationInput
import com.example.moneytracker.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResendEmailConfirmationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun resendEmail(email: String): Response<StringResponse> {
        val resendEmailConfirmationInput = ResendEmailConfirmationInput(email)
        return userRepository.resendEmail(resendEmailConfirmationInput)
    }
}