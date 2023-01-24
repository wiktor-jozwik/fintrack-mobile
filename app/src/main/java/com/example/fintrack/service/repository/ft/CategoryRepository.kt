package com.example.fintrack.service.repository.ft

import com.example.fintrack.service.model.ft.Category
import com.example.fintrack.service.model.ft.inputs.CategoryCreateInput
import com.example.fintrack.view.ui.utils.responseErrorHandler
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val finTrackApi: FinTrackApi,
) {
    suspend fun getAllCategories(): List<Category> =
        responseErrorHandler(finTrackApi.api.getAllCategories())

    suspend fun addNewCategory(category: CategoryCreateInput): Category =
        responseErrorHandler(finTrackApi.api.saveCategory(category))

    suspend fun editCategory(id: Int, category: CategoryCreateInput): Category =
        responseErrorHandler(finTrackApi.api.editCategory(id, category))

    suspend fun deleteCategory(categoryId: Int): Category =
        responseErrorHandler(finTrackApi.api.deleteCategory(categoryId))
}