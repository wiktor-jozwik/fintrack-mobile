package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.model.mt.inputs.OperationCreateInput
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllOperations(): Response<List<Operation>> =
        moneyTrackerApi.api.getAllOperations()

    suspend fun getAllOperationsInRanges(
        startDate: LocalDate,
        endDate: LocalDate
    ): Response<List<Operation>> =
        moneyTrackerApi.api.getAllOperationsInRange(startDate, endDate)

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Response<Operation> =
        moneyTrackerApi.api.saveOperation(operationCreateInput)

    suspend fun editOperation(id: Int, operation: OperationCreateInput): Response<Operation> =
        moneyTrackerApi.api.editOperation(id, operation)

    suspend fun deleteOperation(operationId: Int): Response<Operation> =
        moneyTrackerApi.api.deleteOperation(operationId)
}