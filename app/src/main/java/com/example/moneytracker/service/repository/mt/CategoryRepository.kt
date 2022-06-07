package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Category
import com.example.moneytracker.service.model.mt.inputs.category.CategoryCreateInput
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
) {
    suspend fun getAllCategories(): Response<List<Category>> =
        moneyTrackerApi.api.getAllCategories()

    suspend fun addNewCategory(category: CategoryCreateInput): Response<Category> =
        moneyTrackerApi.api.saveCategory(category)

    suspend fun deleteCategory(categoryId: Int): Response<Category> =
        moneyTrackerApi.api.deleteCategory(categoryId)
}