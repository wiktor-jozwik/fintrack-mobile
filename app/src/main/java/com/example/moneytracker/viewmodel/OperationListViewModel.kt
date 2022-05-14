package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.OperationRepository
import kotlinx.coroutines.launch

class OperationListViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()

    private val operationsResponse: MutableLiveData<List<Operation>> = MutableLiveData()

    fun getAllOperations(): LiveData<List<Operation>> {
        viewModelScope.launch {
            val response = operationRepository.getAllOperations()

            operationsResponse.value = response
        }
        return operationsResponse
    }
}