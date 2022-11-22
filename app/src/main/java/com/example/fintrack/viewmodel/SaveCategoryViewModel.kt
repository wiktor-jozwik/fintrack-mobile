package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.CategoryType
import com.example.fintrack.service.model.mt.inputs.CategoryCreateInput
import com.example.fintrack.service.repository.mt.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    suspend fun addNewCategory(name: String, type: CategoryType, isInternal: Boolean): Category =
        categoryRepository.addNewCategory(CategoryCreateInput(name, type, isInternal))

    suspend fun editCategory(id: Int, name: String, type: CategoryType, isInternal: Boolean): Category =
        categoryRepository.editCategory(id, CategoryCreateInput(name, type, isInternal))
}