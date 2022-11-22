package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Category
import com.example.fintrack.service.repository.ft.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    suspend fun getAllCategories(): List<Category> =
        categoryRepository.getAllCategories()

    suspend fun deleteCategory(categoryId: Int): Category =
        categoryRepository.deleteCategory(categoryId)
}