package com.example.moneytracker.service.repository.exchangerate

import com.example.moneytracker.service.model.exchangerate.ExchangerateConvertedPrice
import com.example.moneytracker.service.model.exchangerate.ExchangerateCurrencyRates
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

    @GET("${Constants.EXCHANGERATE_API_URL}/timeseries")
    suspend fun getCurrencyRates(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("symbols") baseCurrency: String,
        @Query("base") chosenCurrency: String,
    ): ExchangerateCurrencyRates
}