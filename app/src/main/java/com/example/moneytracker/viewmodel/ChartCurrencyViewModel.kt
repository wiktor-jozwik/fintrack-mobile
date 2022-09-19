package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.exchangerate.CurrencyPrice
import com.example.moneytracker.service.model.exchangerate.ExchangerateCurrencyRates
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.service.repository.exchangerate.ExchangerateRepository
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChartCurrencyViewModel @Inject constructor(
    private val exchangerateRepository: ExchangerateRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    enum class Period {
        MONTH,
        YEAR
    }

    suspend fun getSupportedCurrencies(): List<Currency> =
        currencyRepository.getSupportedCurrenciesWithoutDefault()


    suspend fun getHistoricalCurrencyPrice(
        chosenCurrency: String,
        x: Int,
        monthOrYear: Period
    ): Pair<List<Entry>, List<String>> {
        val currencyPrices = mutableListOf<CurrencyPrice>()
        val currencyEntries = mutableListOf<Entry>()
        val xLabels = mutableListOf<String>()

        val baseCurrency: String = currencyRepository.getUserDefaultCurrency().name

        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val currentDate = LocalDate.of(year, month, day)

        when (monthOrYear) {
            Period.MONTH -> {
                val dateFrom = currentDate.minusMonths(x.toLong()).formatToIsoDateWithDashes()
                val dateTo = currentDate.formatToIsoDateWithDashes()

                currencyPrices += getCurrencyPrices(
                    exchangerateRepository.getCurrencyRates(
                        dateFrom,
                        dateTo,
                        baseCurrency,
                        chosenCurrency
                    )
                )
            }
            Period.YEAR -> {
                for (i in x downTo 1) {
                    val dateFrom = currentDate.minusYears(i.toLong()).formatToIsoDateWithDashes()
                    val dateTo =
                        currentDate.minusYears((i - 1).toLong()).formatToIsoDateWithDashes()
                    currencyPrices += getCurrencyPrices(
                        exchangerateRepository.getCurrencyRates(
                            dateFrom,
                            dateTo,
                            baseCurrency,
                            chosenCurrency
                        )
                    )
                }
            }
        }

        (currencyPrices).forEachIndexed { index, currencyPrice ->
            currencyEntries.add(Entry(index.toFloat(), currencyPrice.price.toFloat()))
            xLabels.add(currencyPrice.date)
        }

        return Pair(currencyEntries, xLabels)
    }

    private fun getCurrencyPrices(exchangerateCurrencyRates: ExchangerateCurrencyRates): List<CurrencyPrice> {
        val currencyPrices = mutableListOf<CurrencyPrice>()

        for ((date, tickerMap) in exchangerateCurrencyRates.rates) {
            for ((_, value) in tickerMap) {
                currencyPrices += CurrencyPrice(value, date)
            }
        }

        return currencyPrices
    }
}