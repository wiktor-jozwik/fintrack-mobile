package com.example.moneytracker.service.repository.nbp

import com.example.moneytracker.service.model.nbp.NbpGoldPrice
import javax.inject.Inject

class NbpRepository @Inject constructor(
    private val nbpApi: NbpApi
) {
    suspend fun getGoldPricesInRange(dateFrom: String, dateTo: String): List<NbpGoldPrice> {
        return nbpApi.api.getGoldPricesInRange(dateFrom, dateTo)
    }
}