package com.example.fintrack.service.repository.nbp

import com.example.fintrack.service.model.nbp.NbpGoldPrice
import javax.inject.Inject

class NbpRepository @Inject constructor(
    private val nbpApi: NbpApi
) {
    suspend fun getGoldPricesInRange(dateFrom: String, dateTo: String): List<NbpGoldPrice> {
        return nbpApi.api.getGoldPricesInRange(dateFrom, dateTo)
    }
}