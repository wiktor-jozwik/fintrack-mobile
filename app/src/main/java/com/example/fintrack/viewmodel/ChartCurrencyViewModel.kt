package com.example.fintrack.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.service.repository.ft.CurrencyRepository
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChartCurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    enum class Period {
        MONTH,
        YEAR
    }

    suspend fun getSupportedCurrencies(): List<Currency>  {
        val baseCurrency = sharedPreferences.getString("USER_DEFAULT_CURRENCY", "")
            ?: throw Exception("Base currency is not defined. Contact administrator")

        val usersCurrencies = currencyRepository.getSupportedCurrencies()

        return usersCurrencies.filter {
            it.name != baseCurrency
        }
    }

    suspend fun getHistoricalCurrencyPrice(
        chosenCurrency: String,
        x: Int,
        monthOrYear: Period
    ): Pair<List<Entry>, List<String>> {
        val baseCurrency = sharedPreferences.getString("USER_DEFAULT_CURRENCY", "")
            ?: throw Exception("Base currency is not defined. Contact administrator")

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