package com.example.moneytracker.service.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moneytracker.service.model.Operation
import java.time.LocalDateTime

class OperationRepository {
    private val moneyTrackerApi: IMoneyTrackerApi = MoneyTrackerApi()

    fun getOperationList(): LiveData<List<Operation>> {

        val data: MutableLiveData<List<Operation>> = MutableLiveData<List<Operation>>()

        data.value = moneyTrackerApi.getOperationList().sortedByDescending { it.date }

        return data
    }

    fun getOperationListInRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Operation> {
        return moneyTrackerApi.getOperationsByDateRange(startDate, endDate)
    }

//    fun addNewOperation()
}