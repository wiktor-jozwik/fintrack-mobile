package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Expenses
import com.example.moneytracker.service.model.mt.User
import com.example.moneytracker.service.model.mt.UserProfileData
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.service.repository.mt.UserRepository
import com.example.moneytracker.view.ui.utils.cutText
import com.example.moneytracker.viewmodel.utils.ExpenseCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
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
        val operations = operationRepository.getAllOperations()
        val (income, outcome) = expenseCalculator.calculate(operations)

        return Expenses(income, outcome)
    }

    private fun getValidText(text: String?): String {
        if (text.isNullOrBlank()) {
            return "-"
        }

        return text.cutText(20)
    }
}