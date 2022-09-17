package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Expenses
import com.example.moneytracker.service.model.mt.UserProfileData
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.service.repository.mt.UserRepository
import com.example.moneytracker.view.ui.utils.responseErrorHandler
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

    suspend fun getProfileData(): Response<UserProfileData> {
        return userRepository.getProfileData()
    }

    suspend fun getTotalExpenses(): Expenses {
        val operations = responseErrorHandler(operationRepository.getAllOperations())
        val (income, outcome) = expenseCalculator.calculate(operations)

        return Expenses(income, outcome)
    }
}