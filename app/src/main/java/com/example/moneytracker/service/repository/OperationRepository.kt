package com.example.moneytracker.service.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moneytracker.service.model.Operation
import java.time.LocalDateTime

class OperationRepository {
    private val moneyTrackerApi: IMoneyTrackerApi = MoneyTrackerApi()

    fun getAllOperations(): LiveData<List<Operation>> {
        val data: MutableLiveData<List<Operation>> = MutableLiveData<List<Operation>>()

        data.value = moneyTrackerApi.getAllOperations().sortedByDescending { it.date }

        return data
    }

    fun getAllOperationsInRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Operation> {
        return moneyTrackerApi.getAllOperationsInRange(startDate, endDate)
    }

    fun addNewOperation(name: String, moneyAmount: Double, category: String, currency: String) {
        moneyTrackerApi.saveOperation(name, moneyAmount, category, currency)
    }
}