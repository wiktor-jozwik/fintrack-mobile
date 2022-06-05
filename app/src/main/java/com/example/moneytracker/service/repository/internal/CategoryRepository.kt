package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.inputs.category.CategoryCreateInput
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllCategories(): List<Category> =
        moneyTrackerApi.api.getAllCategories()

    suspend fun addNewCategory(category: CategoryCreateInput): Category =
        moneyTrackerApi.api.saveCategory(category)

    suspend fun deleteCategory(categoryId: Int): Category =
        moneyTrackerApi.api.deleteCategory(categoryId)
}