package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import com.example.moneytracker.service.model.OperationCategoryType
import com.example.moneytracker.service.model.OperationCreateInput
import com.example.moneytracker.service.repository.OperationCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: OperationCategoryRepository,
): ViewModel() {

    private val categorySaveResponse: MutableLiveData<OperationCategory> = MutableLiveData()

    suspend fun addNewCategory(name: String, type: OperationCategoryType): LiveData<OperationCategory> {
        val categoryCreateInput = OperationCategory(null, name, type)

        categorySaveResponse.value = categoryRepository.addNewCategory(categoryCreateInput)

        return categorySaveResponse
    }
}