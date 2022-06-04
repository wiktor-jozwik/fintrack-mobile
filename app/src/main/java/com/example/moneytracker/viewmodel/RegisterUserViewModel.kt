package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.User
import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterForm
import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterInput
import com.example.moneytracker.service.repository.internal.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val userRegisterResponse: MutableLiveData<User> = MutableLiveData()

    suspend fun registerUser(
        email: String,
        password: String,
        passwordConfirmation: String
    ): MutableLiveData<User> {
        val userRegisterInput = UserRegisterInput(UserRegisterForm(email, password, passwordConfirmation))

        userRegisterResponse.value = userRepository.registerUser(userRegisterInput)

        return userRegisterResponse
    }
}