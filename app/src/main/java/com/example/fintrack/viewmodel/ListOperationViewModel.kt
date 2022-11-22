package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Operation
import com.example.fintrack.service.model.ft.OperationSearchFilters
import com.example.fintrack.service.repository.ft.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListOperationViewModel @Inject constructor(
    private val operationRepository: OperationRepository
) : ViewModel() {
    suspend fun getAllOperations(searchFilters: OperationSearchFilters?): List<Operation> {
        var filters = searchFilters
        if (filters == null) {
            filters = OperationSearchFilters(null, null, null, null, null, null, null)
        }
        return operationRepository.getAllOperations(filters)
    }

    suspend fun deleteOperation(operationId: Int): Operation =
        operationRepository.deleteOperation(operationId)
}