package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.createinputs.CategoryCreateInput
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllCategories(): List<Category> {
        return moneyTrackerApi.api.getAllCategories()
    }

    suspend fun addNewCategory(category: CategoryCreateInput): Category {
        return moneyTrackerApi.api.saveCategory(category)
    }

    suspend fun deleteCategory(categoryId: Int): Category {
        return moneyTrackerApi.api.deleteCategory(categoryId)
    }
}