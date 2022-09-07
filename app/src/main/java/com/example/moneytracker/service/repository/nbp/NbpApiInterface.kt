package com.example.moneytracker.service.repository.nbp

import com.example.moneytracker.service.model.nbp.NbpGoldPrice
import com.example.moneytracker.service.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path

interface NbpApiInterface {
    @GET("${Constants.NBP_API_URL}${Constants.NBP_GOLD_ENDPOINT}/{dateFrom}/{dateTo}/?format=json")
    suspend fun getGoldPricesInRange(
        @Path("dateFrom") dateFrom: String,
        @Path("dateTo") dateTo: String,
    ): List<NbpGoldPrice>
}