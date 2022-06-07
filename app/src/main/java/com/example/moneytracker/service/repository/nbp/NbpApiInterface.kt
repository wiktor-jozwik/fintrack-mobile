package com.example.moneytracker.service.repository.nbp

import com.example.moneytracker.service.model.nbp.NpbCurrencyAtDay
import com.example.moneytracker.service.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path

interface NbpApiInterface {
    @GET("${Constants.NBP_API_URL}rates/{table}/{currency}/{date}/?format=json")
    suspend fun getCurrencyAtDay(
        @Path("currency") currency: String,
        @Path("date") date: String,
        @Path("table") table: String = "A",
    ): NpbCurrencyAtDay
}