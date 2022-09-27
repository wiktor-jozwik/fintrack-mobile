package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.repository.mt.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class ImportOperationsViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
) : ViewModel() {
    suspend fun importOperations(file: MultipartBody.Part, csvImportWayText: String) {
        val csvImportWay: RequestBody = RequestBody.create(MediaType.parse("text/plain"), csvImportWayText)

        operationRepository.importOperations(file, csvImportWay)
    }

    suspend fun getSupportedCsvImportWays() =
        operationRepository.getSupportedCsvImportWays()
}