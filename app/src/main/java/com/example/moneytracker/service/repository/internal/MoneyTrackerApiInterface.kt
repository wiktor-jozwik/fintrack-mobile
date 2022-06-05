package com.example.moneytracker.service.repository.internal

import com.example.moneytracker.service.model.*
import com.example.moneytracker.service.model.createinputs.CategoryCreateInput
import com.example.moneytracker.service.model.createinputs.OperationCreateInput
import com.example.moneytracker.service.model.createinputs.userlogin.UserLoginInput
import com.example.moneytracker.service.model.createinputs.userregister.UserRegisterInput
import com.example.moneytracker.service.utils.Constants.Companion.MONEY_TRACKER_API_ENDPOINT
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
    suspend fun getAllCategories(): List<Category>

    @GET("${MONEY_TRACKER_API_ENDPOINT}/currencies")
    suspend fun getAllCurrencies(): List<Currency>

    @POST("${MONEY_TRACKER_API_ENDPOINT}/operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Operation

    @DELETE("${MONEY_TRACKER_API_ENDPOINT}/operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Operation

    @POST("${MONEY_TRACKER_API_ENDPOINT}/categories")
    suspend fun saveCategory(@Body category: CategoryCreateInput): Category

    @DELETE("${MONEY_TRACKER_API_ENDPOINT}/categories/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Category

    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterInput): User

    @POST("login")
    suspend fun loginUser(@Body user: UserLoginInput): JwtResponse

    @DELETE("logout")
    suspend fun logoutUser(): ApiResponse
}