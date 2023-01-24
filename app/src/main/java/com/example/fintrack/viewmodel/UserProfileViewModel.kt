package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Expenses
import com.example.fintrack.service.model.ft.User
import com.example.fintrack.service.model.ft.UserProfileData
import com.example.fintrack.service.repository.ft.OperationRepository
import com.example.fintrack.service.repository.ft.UserRepository
import com.example.fintrack.view.ui.utils.cutText
import com.example.fintrack.viewmodel.utils.ExpenseCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val operationRepository: OperationRepository,
    private val expenseCalculator: ExpenseCalculator
) : ViewModel() {

    suspend fun getProfileData(): UserProfileData {
        val userProfileData = userRepository.getProfileData()

        var (email, firstName, lastName, phoneNumber) = userProfileData.user

        email = getValidText(email)
        firstName = getValidText(firstName)
        lastName = getValidText(lastName)
        phoneNumber = getValidText(phoneNumber)
        email = getValidText(email)

        return UserProfileData(
            defaultCurrency = userProfileData.defaultCurrency,
            user = User(
                email, firstName, lastName, phoneNumber
            )
        )
    }


    suspend fun getTotalExpenses(): Expenses {
        val yearlyOperations = operationRepository.getAllOperationsInDefaultCurrency(null, null)

        val (income, outcome) = expenseCalculator.calculate(yearlyOperations)

        return Expenses(income, outcome)
    }

    private fun getValidText(text: String?): String {
        if (text.isNullOrBlank()) {
            return "-"
        }

        return text.cutText(20)
    }
}