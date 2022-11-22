package com.example.fintrack.service.repository.mt

import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.inputs.CategoryCreateInput
import com.example.fintrack.view.ui.utils.responseErrorHandler
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllCategories(): List<Category> =
        responseErrorHandler(moneyTrackerApi.api.getAllCategories())

    suspend fun addNewCategory(category: CategoryCreateInput): Category =
        responseErrorHandler(moneyTrackerApi.api.saveCategory(category))

    suspend fun editCategory(id: Int, category: CategoryCreateInput): Category =
        responseErrorHandler(moneyTrackerApi.api.editCategory(id, category))

    suspend fun deleteCategory(categoryId: Int): Category =
        responseErrorHandler(moneyTrackerApi.api.deleteCategory(categoryId))
}