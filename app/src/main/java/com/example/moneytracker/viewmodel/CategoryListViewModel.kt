package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val categoriesResponse: MutableLiveData<List<Category>> = MutableLiveData()
    private val deleteResponse: MutableLiveData<Category> = MutableLiveData()

    suspend fun getAllCategories(): MutableLiveData<List<Category>> {
        categoriesResponse.value = categoryRepository.getAllCategories()

        return categoriesResponse
    }

    suspend fun deleteOperation(categoryId: Int): MutableLiveData<Category> {
        deleteResponse.value = categoryRepository.deleteCategory(categoryId)

        return deleteResponse
    }
}