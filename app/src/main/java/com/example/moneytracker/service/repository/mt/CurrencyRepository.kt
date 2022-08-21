package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.repository.nbp.NbpApi
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val moneyTrackerApi: MoneyTrackerApi,
    private val nbpApi: NbpApi
) {
    private val defaultCurrencyName = "PLN"

    suspend fun getUsersCurrencies(): Response<List<Currency>> =
        moneyTrackerApi.api.getUsersCurrencies()

    suspend fun getSupportedCurrencies(): Response<List<Currency>> =
        moneyTrackerApi.api.getAllCurrencies()

    suspend fun getPriceOfCurrencyAtDay(currency: String, date: LocalDate): Double {
        if (currency == defaultCurrencyName) {
            return 1.0
        }

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, date.year)
        calendar.set(Calendar.MONTH, date.monthValue - 1)
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)

        val notWeekendDate = when {
            calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY -> date.minusDays(1)
            calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY -> date.minusDays(2)
            else -> date
        }

        val currencyAtDay =
            nbpApi.api.getCurrencyAtDay(currency, notWeekendDate.formatToIsoDateWithDashes())

        return currencyAtDay.rates.first().mid
    }
}