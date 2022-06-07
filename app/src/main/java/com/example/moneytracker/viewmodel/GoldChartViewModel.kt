package com.example.moneytracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.nbp.NbpGoldPrice
import com.example.moneytracker.service.repository.nbp.NbpRepository
import com.example.moneytracker.utils.formatToIsoDateWithDashes
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@HiltViewModel
class GoldChartViewModel @Inject constructor(
    private val nbpRepository: NbpRepository,
) : ViewModel() {
    enum class Period {
        MONTH,
        YEAR
    }
    private val OUNCE_WEIGHT = 31.1034768
    suspend fun getHistoricalGoldPrice(x: Int, monthOrYear: Period): Pair<List<Entry>, List<String>> {
        val goldPrices = mutableListOf<NbpGoldPrice>()
        val goldEntries = mutableListOf<Entry>()
        val xLabels = mutableListOf<String>()

        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val currentDate = LocalDate.of(year, month, day)

        when (monthOrYear) {
            Period.MONTH -> {
                val dateFrom = currentDate.minusMonths(x.toLong()).formatToIsoDateWithDashes()
                val dateTo = currentDate.formatToIsoDateWithDashes()

                goldPrices += nbpRepository.getGoldPricesInRange(dateFrom, dateTo)
            }
            Period.YEAR -> {
                for (i in x downTo 1) {
                    val dateFrom = currentDate.minusYears(i.toLong()).formatToIsoDateWithDashes()
                    val dateTo = currentDate.minusYears((i-1).toLong()).formatToIsoDateWithDashes()
                    goldPrices += nbpRepository.getGoldPricesInRange(dateFrom, dateTo)
                }
            }
        }

        (goldPrices).forEachIndexed { index, nbpGold ->
            goldEntries.add(Entry(index.toFloat(), (nbpGold.price * OUNCE_WEIGHT).toFloat()))
            xLabels.add(nbpGold.date)
        }

        return Pair(goldEntries, xLabels)
    }
}