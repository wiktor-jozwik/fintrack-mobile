package com.example.moneytracker.service.repository.exchangerate

import com.example.moneytracker.service.model.exchangerate.ExchangerateConvertedPrice
import com.example.moneytracker.service.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangerateInterface {
    @GET("${Constants.EXCHANGERATE_API_URL}/convert")
    suspend fun convertCurrency(
        @Query("from") currencyFrom: String,
        @Query("to") currencyTo: String,
        @Query("date") date: String,
        @Query("amount") amount: String,
    ): ExchangerateConvertedPrice
}