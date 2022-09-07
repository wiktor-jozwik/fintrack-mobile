package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.*
import com.example.moneytracker.service.model.mt.inputs.CategoryCreateInput
import com.example.moneytracker.service.model.mt.inputs.OperationCreateInput
import com.example.moneytracker.service.model.mt.inputs.UserLoginInput
import com.example.moneytracker.service.model.mt.inputs.UserRegisterInput
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface MoneyTrackerApiInterface {
    @GET("operations")
    suspend fun getAllOperations(): List<Operation>

    @GET("operations")
    suspend fun getAllOperationsInRange(
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): List<Operation>

    @GET("categories")
    suspend fun getAllCategories(): Response<List<Category>>

    @GET("users_currencies")
    suspend fun getUsersCurrencies(): Response<List<Currency>>

    @GET("users_currencies/default")
    suspend fun getUserDefaultCurrency(): Response<Currency>

    @GET("currencies")
    suspend fun getAllCurrencies(): Response<List<Currency>>

    @POST("operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Response<Operation>

    @DELETE("operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Operation

    @POST("categories")
    suspend fun saveCategory(@Body category: CategoryCreateInput): Response<Category>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Response<Category>

    @POST("auth/register")
    suspend fun registerUser(@Body user: UserRegisterInput): Response<User>

    @POST("auth/login")
    suspend fun loginUser(@Body user: UserLoginInput): Response<JwtResponse>

//    @DELETE("logout")
//    suspend fun logoutUser(): Response<ApiResponse>
}