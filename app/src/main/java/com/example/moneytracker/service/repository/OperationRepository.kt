package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCreateInput
import java.time.LocalDate

class OperationRepository {
    suspend fun getAllOperations(): List<Operation> {
        return MoneyTrackerApi.api.getAllOperations().sortedByDescending { it.date }
    }

    suspend fun getAllOperationsInRanges(startDate: LocalDate, endDate: LocalDate): List<Operation> {
        return MoneyTrackerApi.api.getAllOperationsInRange(startDate, endDate)
    }

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Operation {
        return MoneyTrackerApi.api.saveOperation(operationCreateInput)
    }

    suspend fun deleteOperation(operationId: Int): Operation {
        return MoneyTrackerApi.api.deleteOperation(operationId)
    }
}