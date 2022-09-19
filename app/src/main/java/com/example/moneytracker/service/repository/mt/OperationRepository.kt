package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.model.mt.inputs.OperationCreateInput
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import java.time.LocalDate
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllOperations(): List<Operation> =
        responseErrorHandler(moneyTrackerApi.api.getAllOperations())

    suspend fun getAllOperationsInRanges(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Operation> =
        responseErrorHandler(moneyTrackerApi.api.getAllOperationsInRange(startDate, endDate))

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Operation =
        responseErrorHandler(moneyTrackerApi.api.saveOperation(operationCreateInput))

    suspend fun editOperation(id: Int, operation: OperationCreateInput): Operation =
        responseErrorHandler(moneyTrackerApi.api.editOperation(id, operation))

    suspend fun deleteOperation(operationId: Int): Operation =
        responseErrorHandler(moneyTrackerApi.api.deleteOperation(operationId))
}