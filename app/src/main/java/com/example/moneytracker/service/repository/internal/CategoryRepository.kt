package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.inputs.category.CategoryCreateInput
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllCategories(): Response<List<Category>> =
        moneyTrackerApi.api.getAllCategories()

    suspend fun addNewCategory(category: CategoryCreateInput): Response<Category> =
        moneyTrackerApi.api.saveCategory(category)

    suspend fun deleteCategory(categoryId: Int): Category =
        moneyTrackerApi.api.deleteCategory(categoryId)
}