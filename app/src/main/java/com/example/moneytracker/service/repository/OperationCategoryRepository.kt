package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.OperationCategory

class OperationCategoryRepository {
    private val moneyTrackerApi: IMoneyTrackerApi = MoneyTrackerApi()

    fun getAllCategories(): List<OperationCategory> {
        return moneyTrackerApi.getAllCategories()
    }
}