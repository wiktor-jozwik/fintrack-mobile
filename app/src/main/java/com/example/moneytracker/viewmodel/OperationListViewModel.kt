package com.example.moneytracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.OperationRepository

class OperationListViewModel: ViewModel() {
    private val operationListObservable: LiveData<List<Operation>>
    private val operationRepository: OperationRepository = OperationRepository()

    init {
        operationListObservable = operationRepository.getOperationList();
    }

    fun getOperationListObservable(): LiveData<List<Operation>> {
        Log.i("operations", operationListObservable.toString())
        return operationListObservable
    }
}