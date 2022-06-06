package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.inputs.operation.OperationCreateInput
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllOperations(): List<Operation> =
        moneyTrackerApi.api.getAllOperations().sortedByDescending { it.date }

    suspend fun getAllOperationsInRanges(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Operation> =
        moneyTrackerApi.api.getAllOperationsInRange(startDate, endDate)

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Response<Operation> =
        moneyTrackerApi.api.saveOperation(operationCreateInput)

    suspend fun deleteOperation(operationId: Int): Operation =
        moneyTrackerApi.api.deleteOperation(operationId)
}