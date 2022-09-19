package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.inputs.CategoryCreateInput
import com.example.moneytracker.service.repository.mt.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    suspend fun addNewCategory(name: String, type: CategoryType): Category =
        categoryRepository.addNewCategory(CategoryCreateInput(name, type))

    suspend fun editCategory(id: Int, name: String, type: CategoryType): Category =
        categoryRepository.editCategory(id, CategoryCreateInput(name, type))
}