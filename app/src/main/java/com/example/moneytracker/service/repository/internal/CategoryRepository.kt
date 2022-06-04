package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.createinputs.CategoryCreateInput

class CategoryRepository {
    suspend fun getAllCategories(): List<Category> {
        return MoneyTrackerApi.api.getAllCategories()
    }

    suspend fun addNewCategory(category: CategoryCreateInput): Category {
        return MoneyTrackerApi.api.saveCategory(category)
    }

    suspend fun deleteCategory(categoryId: Int): Category {
        return MoneyTrackerApi.api.deleteCategory(categoryId)
    }
}