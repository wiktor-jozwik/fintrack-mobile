package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.model.mt.OperationSearchFilters
import com.example.moneytracker.service.repository.mt.OperationRepository
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