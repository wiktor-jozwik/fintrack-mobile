package com.example.fintrack.service.repository.ft

import com.example.fintrack.service.model.ft.DefaultCurrencyOperation
import com.example.fintrack.service.model.ft.Operation
import com.example.fintrack.service.model.ft.OperationSearchFilters
import com.example.fintrack.service.model.ft.StringResponse
import com.example.fintrack.service.model.ft.inputs.OperationCreateInput
import com.example.fintrack.view.ui.utils.responseErrorHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDate
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val finTrackApi: FinTrackApi,
) {
    suspend fun getAllOperations(
        operationSearchFilters: OperationSearchFilters
    ): List<Operation> {
        val (startDate, endDate, categoryType, searchName, includeInternal, operator, moneyAmount) = operationSearchFilters
        return responseErrorHandler(
            finTrackApi.api.getAllOperations(
                startDate,
                endDate,
                categoryType,
                searchName,
                includeInternal,
                operator,
                moneyAmount
            )
        )
    }

    suspend fun getAllOperationsInDefaultCurrency(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<DefaultCurrencyOperation> =
        responseErrorHandler(
            finTrackApi.api.getAllOperationsInDefaultCurrency(
                startDate,
                endDate
            )
        )

    suspend fun addNewOperation(operationCreateInput: OperationCreateInput): Operation =
        responseErrorHandler(finTrackApi.api.saveOperation(operationCreateInput))

    suspend fun editOperation(id: Int, operation: OperationCreateInput): Operation =
        responseErrorHandler(finTrackApi.api.editOperation(id, operation))

    suspend fun deleteOperation(operationId: Int): Operation =
        responseErrorHandler(finTrackApi.api.deleteOperation(operationId))

    suspend fun importOperations(
        file: MultipartBody.Part,
        csvImportWay: RequestBody
    ): StringResponse =
        responseErrorHandler(finTrackApi.api.importOperations(file, csvImportWay))

    suspend fun getSupportedCsvImportWays(): List<String> =
        responseErrorHandler(finTrackApi.api.getSupportedCsvImportWays())
}