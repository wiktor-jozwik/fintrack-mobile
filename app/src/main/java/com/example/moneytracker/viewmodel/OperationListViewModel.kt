package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class OperationListViewModel @Inject constructor(
    private val operationRepository: OperationRepository
) : ViewModel() {
    suspend fun getAllOperations(): Response<List<Operation>> {
        return operationRepository.getAllOperations()
    }

    suspend fun deleteOperation(operationId: Int): Response<Operation> {
        return operationRepository.deleteOperation(operationId)
    }
}