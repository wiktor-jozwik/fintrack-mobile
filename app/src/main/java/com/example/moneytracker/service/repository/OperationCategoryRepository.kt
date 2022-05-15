package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.OperationCategory

class OperationCategoryRepository {
    suspend fun getAllCategories(): List<OperationCategory> {
        return MoneyTrackerApi.api.getAllCategories()
    }

    suspend fun addNewCategory(category: OperationCategory): OperationCategory {
        return MoneyTrackerApi.api.saveCategory(category)
    }
}