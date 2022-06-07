package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OperationListViewModel @Inject constructor(
    private val operationRepository: OperationRepository
) : ViewModel() {
    private val operationsResponse: MutableLiveData<List<Operation>> = MutableLiveData()
    private val deleteResponse: MutableLiveData<Operation> = MutableLiveData()

    suspend fun getAllOperations(): MutableLiveData<List<Operation>> {
        operationsResponse.value = operationRepository.getAllOperations()

        return operationsResponse
    }

    suspend fun deleteOperation(operationId: Int): MutableLiveData<Operation> {
        deleteResponse.value = operationRepository.deleteOperation(operationId)

        return deleteResponse
    }
}