package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.repository.mt.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ListCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    suspend fun getAllCategories(): Response<List<Category>> {
        return categoryRepository.getAllCategories()
    }

    suspend fun deleteCategory(categoryId: Int): Response<Category> {
        return categoryRepository.deleteCategory(categoryId)
    }
}