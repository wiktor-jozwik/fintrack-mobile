package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.StringResponse
import com.example.fintrack.service.model.mt.inputs.ResendEmailConfirmationInput
import com.example.fintrack.service.repository.mt.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResendEmailConfirmationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun resendEmail(email: String): StringResponse =
        userRepository.resendEmail(ResendEmailConfirmationInput(email))
}