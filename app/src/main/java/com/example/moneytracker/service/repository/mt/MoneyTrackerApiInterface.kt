package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.*
import com.example.moneytracker.service.model.mt.inputs.*
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface MoneyTrackerApiInterface {
    @GET("operations")
    suspend fun getAllOperations(): Response<List<Operation>>

    @GET("operations")
    suspend fun getAllOperationsInRange(
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): Response<List<Operation>>

    @GET("categories")
    suspend fun getAllCategories(): Response<List<Category>>

    @GET("users_currencies")
    suspend fun getUserCurrencies(): Response<List<Currency>>

    @GET("users_currencies/default")
    suspend fun getUserDefaultCurrency(): Response<Currency>

    @GET("users_currencies/without_default")
    suspend fun getSupportedCurrenciesWithoutDefault(): Response<List<Currency>>

    @POST("users_currencies")
    suspend fun saveUserCurrency(@Body currency: CurrencyCreateInput): Response<Currency>

    @DELETE("users_currencies/{id}")
    suspend fun deleteUserCurrency(@Path("id") userCurrencyId: Int): Response<Currency>

    @GET("currencies")
    suspend fun getAllCurrencies(): Response<List<Currency>>

    @POST("operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Response<Operation>

    @PATCH("operations/{id}")
    suspend fun editOperation(
        @Path("id") operationId: Int,
        @Body operation: OperationCreateInput
    ): Response<Operation>

    @DELETE("operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Response<Operation>

    @POST("categories")
    suspend fun saveCategory(@Body category: CategoryCreateInput): Response<Category>

    @PATCH("categories/{id}")
    suspend fun editCategory(
        @Path("id") categoryId: Int,
        @Body category: CategoryCreateInput
    ): Response<Category>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Response<Category>

    @POST("auth/register")
    suspend fun registerUser(@Body user: UserRegisterInput): Response<User>

    @POST("auth/login")
    suspend fun loginUser(@Body user: UserLoginInput): Response<JwtResponse>

    @POST("users/resend_activation_email")
    suspend fun resendEmail(@Body resendEmailConfirmationInput: ResendEmailConfirmationInput): Response<StringResponse>

//    @DELETE("logout")
//    suspend fun logoutUser(): Response<ApiResponse>
}