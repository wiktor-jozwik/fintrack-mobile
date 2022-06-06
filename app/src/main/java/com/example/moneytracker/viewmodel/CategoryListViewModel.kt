package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.repository.internal.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val deleteResponse: MutableLiveData<Category> = MutableLiveData()

    suspend fun getAllCategories(): Response<List<Category>> {
        return categoryRepository.getAllCategories()
    }

    suspend fun deleteOperation(categoryId: Int): MutableLiveData<Category> {
        deleteResponse.value = categoryRepository.deleteCategory(categoryId)

        return deleteResponse
    }
}