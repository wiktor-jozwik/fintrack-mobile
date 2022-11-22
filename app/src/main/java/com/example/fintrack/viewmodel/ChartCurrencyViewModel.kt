package com.example.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.mt.Currency
import com.example.fintrack.service.repository.mt.CurrencyRepository
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChartCurrencyViewModel @Inject constructor(
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
        val baseCurrency: String = currencyRepository.getUserDefaultCurrency().name

        val calendar = Calendar.getInstance();

        val currentDate = LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        val startDate: LocalDate = when (monthOrYear) {
            Period.MONTH -> {
                currentDate.minusMonths(x.toLong())
            }
            Period.YEAR -> {
                currentDate.minusYears(x.toLong())
            }
        }

        val currencyRates = currencyRepository.getCurrencyRates(
            baseCurrency,
            chosenCurrency,
            startDate,
            currentDate
        )

        val currencyEntries = mutableListOf<Entry>()
        val xLabels = mutableListOf<String>()

        (currencyRates).forEachIndexed { index, currencyPrice ->
            currencyEntries.add(Entry(index.toFloat(), currencyPrice.value.toFloat()))
            xLabels.add(currencyPrice.date)
        }

        return Pair(currencyEntries, xLabels)
    }
}