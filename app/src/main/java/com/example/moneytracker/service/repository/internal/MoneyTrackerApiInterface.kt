package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.*
import com.example.moneytracker.service.model.inputs.category.CategoryCreateInput
import com.example.moneytracker.service.model.inputs.operation.OperationCreateInput
import com.example.moneytracker.service.model.inputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.inputs.userregister.UserRegisterInput
import com.example.moneytracker.service.utils.Constants.Companion.MONEY_TRACKER_API_ENDPOINT
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface MoneyTrackerApiInterface {
    @GET("${MONEY_TRACKER_API_ENDPOINT}/operations")
    suspend fun getAllOperations(): List<Operation>

    @GET("${MONEY_TRACKER_API_ENDPOINT}/operations")
    suspend fun getAllOperationsInRange(
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate
    ): List<Operation>

    @GET("${MONEY_TRACKER_API_ENDPOINT}/categories")
    suspend fun getAllCategories(): Response<List<Category>>

    @GET("${MONEY_TRACKER_API_ENDPOINT}/currencies")
    suspend fun getAllCurrencies(): Response<List<Currency>>

    @POST("${MONEY_TRACKER_API_ENDPOINT}/operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Response<Operation>

    @DELETE("${MONEY_TRACKER_API_ENDPOINT}/operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Operation

    @POST("${MONEY_TRACKER_API_ENDPOINT}/categories")
    suspend fun saveCategory(@Body category: CategoryCreateInput): Response<Category>

    @DELETE("${MONEY_TRACKER_API_ENDPOINT}/categories/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Response<Category>

    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterInput): Response<User>

    @POST("login")
    suspend fun loginUser(@Body user: UserLoginInput): Response<JwtResponse>

    @DELETE("logout")
    suspend fun logoutUser(): Response<ApiResponse>
}