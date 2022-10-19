package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListOperationViewModel @Inject constructor(
    private val operationRepository: OperationRepository
) : ViewModel() {
    suspend fun getAllOperations(): List<Operation> =
        operationRepository.getAllOperationsInRanges(null, null)

    suspend fun deleteOperation(operationId: Int): Operation =
        operationRepository.deleteOperation(operationId)
}