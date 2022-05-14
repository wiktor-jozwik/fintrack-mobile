package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.OperationCategory

class OperationCategoryRepository {
    suspend fun getAllCategories(): List<OperationCategory> {
        return MoneyTrackerApi.api.getAllCategories()
    }
}