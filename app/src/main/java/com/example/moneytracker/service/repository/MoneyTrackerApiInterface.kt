package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.Category
import com.example.moneytracker.service.model.create_inputs.CategoryCreateInput
import com.example.moneytracker.service.model.create_inputs.OperationCreateInput
import retrofit2.http.*
import java.time.LocalDate

interface MoneyTrackerApiInterface {
    @GET("operations")
    suspend fun getAllOperations(): List<Operation>

    @GET("operations")
    suspend fun getAllOperationsInRange(
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate
    ): List<Operation>

    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("currencies")
    suspend fun getAllCurrencies(): List<Currency>

    @POST("operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Operation

    @DELETE("operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Operation

    @POST("categories")
    suspend fun saveCategory(@Body category: CategoryCreateInput): Category

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Category
}