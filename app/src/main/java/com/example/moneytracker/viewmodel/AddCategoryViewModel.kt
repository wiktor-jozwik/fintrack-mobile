package com.example.moneytracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.CategoryType
import com.example.moneytracker.service.model.createinputs.CategoryCreateInput
import com.example.moneytracker.service.repository.internal.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val categorySaveResponse: MutableLiveData<Category> = MutableLiveData()

    suspend fun addNewCategory(name: String, type: CategoryType): LiveData<Category> {
        val categoryCreateInput = CategoryCreateInput(name, type)

        categorySaveResponse.value = categoryRepository.addNewCategory(categoryCreateInput)

        return categorySaveResponse
    }
}