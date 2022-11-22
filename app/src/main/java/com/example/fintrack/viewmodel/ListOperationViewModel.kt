package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.Operation
import com.example.fintrack.service.model.mt.OperationSearchFilters
import com.example.fintrack.service.repository.mt.OperationRepository
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