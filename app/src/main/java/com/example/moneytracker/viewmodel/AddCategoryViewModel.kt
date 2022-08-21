package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.inputs.CategoryCreateInput
import com.example.moneytracker.service.repository.mt.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    suspend fun addNewCategory(name: String, type: CategoryType): Response<Category> {
        val categoryCreateInput = CategoryCreateInput(name, type)

        return categoryRepository.addNewCategory(categoryCreateInput)
    }
}