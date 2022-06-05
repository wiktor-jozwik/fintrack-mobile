package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.createinputs.OperationCreateInput
import java.time.LocalDate
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllOperations(): List<Operation> {
        return moneyTrackerApi.api.getAllOperations().sortedByDescending { it.date }
    }

    suspend fun getAllOperationsInRanges(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Operation> {
        return moneyTrackerApi.api.getAllOperationsInRange(startDate, endDate)
    }

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Operation {
        return moneyTrackerApi.api.saveOperation(operationCreateInput)
    }

    suspend fun deleteOperation(operationId: Int): Operation {
        return moneyTrackerApi.api.deleteOperation(operationId)
    }
}