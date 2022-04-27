package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.OperationRepository

class OperationListViewModel: ViewModel() {
    private val operationRepository: OperationRepository = OperationRepository()

    fun getOperationListObservable(): LiveData<List<Operation>> {
        return operationRepository.getOperationList()
    }
}