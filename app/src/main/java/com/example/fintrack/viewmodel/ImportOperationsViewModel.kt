package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.StringResponse
import com.example.fintrack.service.repository.ft.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class ImportOperationsViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
) : ViewModel() {
    suspend fun importOperations(file: MultipartBody.Part, csvImportWayText: String): StringResponse {
        val csvImportWay: RequestBody = RequestBody.create(MediaType.parse("text/plain"), csvImportWayText)

        return operationRepository.importOperations(file, csvImportWay)
    }

    suspend fun getSupportedCsvImportWays(): List<String> =
        operationRepository.getSupportedCsvImportWays()
}