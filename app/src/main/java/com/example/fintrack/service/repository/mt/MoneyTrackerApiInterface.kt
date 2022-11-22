package com.example.fintrack.service.repository.mt

import com.example.fintrack.service.model.mt.*
import com.example.fintrack.service.model.mt.inputs.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface MoneyTrackerApiInterface {
    @GET("operations")
    suspend fun getAllOperations(
        @Query("startDate") startDate: LocalDate?,
        @Query("endDate") endDate: LocalDate?,
        @Query("categoryType") categoryType: CategoryType?,
        @Query("searchName") searchName: String?,
        @Query("includeInternal") includeInternal: Boolean?,
        @Query("operator") operator: String?,
        @Query("moneyAmount") moneyAmount: Double?,
    ): Response<List<Operation>>

    @GET("operations/default_currency")
    suspend fun getAllOperationsInDefaultCurrency(
        @Query("startDate") startDate: LocalDate?,
        @Query("endDate") endDate: LocalDate?
    ): Response<List<DefaultCurrencyOperation>>

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

    @GET("currency_rates")
    suspend fun getCurrencyRates(
        @Query("baseCurrency") baseCurrency: String,
        @Query("currency") currency: String,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): Response<List<CurrencyRate>>

    @POST("operations")
    suspend fun saveOperation(@Body operation: OperationCreateInput): Response<Operation>

    @PATCH("operations/{id}")
    suspend fun editOperation(
        @Path("id") operationId: Int,
        @Body operation: OperationCreateInput
    ): Response<Operation>

    @DELETE("operations/{id}")
    suspend fun deleteOperation(@Path("id") operationId: Int): Response<Operation>

    @Multipart
    @POST("operations_import")
    suspend fun importOperations(
        @Part file: MultipartBody.Part,
        @Part("csvImportWay") csvImportWay: RequestBody
    ): Response<StringResponse>

    @GET("operations_import/supported_csv_ways")
    suspend fun getSupportedCsvImportWays(): Response<List<String>>

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

    @DELETE("auth/logout")
    suspend fun logoutUser(): Response<LogoutResponse>

    @POST("users/resend_activation_email")
    suspend fun resendEmail(@Body resendEmailConfirmationInput: ResendEmailConfirmationInput): Response<StringResponse>

    @GET("users/profile")
    suspend fun getProfileData(): Response<UserProfileData>
}