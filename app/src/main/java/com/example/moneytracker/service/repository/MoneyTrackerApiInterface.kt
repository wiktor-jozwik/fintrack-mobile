package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Currency
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import com.example.moneytracker.service.model.OperationCreateInput
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalDateTime

interface MoneyTrackerApiInterface {
    @GET("operations")
    suspend fun getAllOperations(): List<Operation>

//    fun getAllOperations(): List<Operation>

    @GET("operations")
    suspend fun getAllOperationsInRange(
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate
    ): List<Operation>

    @GET("categories")
    suspend fun getAllCategories(): List<OperationCategory>

//    fun getAllCategories(): List<OperationCategory>

    @GET("currencies")
    suspend fun getAllCurrencies(): List<Currency>

    @POST("operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput) : Operation

    fun saveCategory(category: OperationCategory)
}