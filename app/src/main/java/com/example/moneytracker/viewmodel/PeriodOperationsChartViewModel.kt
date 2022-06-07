package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.viewmodel.utils.CurrencyCalculator
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PeriodOperationsChartViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyCalculator: CurrencyCalculator
) : ViewModel() {

    suspend fun getChartData(
        x: Int = 6,
        allTime: Boolean = false
    ): Triple<List<BarEntry>, List<BarEntry>, Boolean> {
        if (allTime) {
            val allTimeOperations = getAllTimeOperations()
            return Triple(allTimeOperations.first, allTimeOperations.second, allTime)
        }
        val monthOperations = getLastXMonthsOperations(x)

        return Triple(monthOperations.first, monthOperations.second, allTime)
    }

    private suspend fun getLastXMonthsOperations(x: Int): Pair<List<BarEntry>, List<BarEntry>> {
        val incomesAndOutcomesYearlyResponse: MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> =
            MutableLiveData()

        val fromDate = LocalDate.now(ZoneId.systemDefault()).minusMonths((x - 1).toLong())
            .with(TemporalAdjusters.firstDayOfMonth());
        val toDate = LocalDate.now(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());

        val operations = operationRepository.getAllOperationsInRanges(fromDate, toDate);

        val monthsRequired = mutableListOf<Int>()
        var monthToInsert = fromDate.monthValue
        for (i in 1..x) {
            monthsRequired.add(monthToInsert)
            monthToInsert++
            if (monthToInsert == 13) {
                monthToInsert = 1
            }
        }

        return groupMonthlyOperations(operations, monthsRequired.sortedBy { it })

//        return incomesAndOutcomesYearlyResponse
    }

    private suspend fun getAllTimeOperations(): Pair<List<BarEntry>, List<BarEntry>> {
        val incomesAndOutcomesYearlyResponse: MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> =
            MutableLiveData()

        val operations = operationRepository.getAllOperations()
        return groupYearlyOperations(operations)

//        return incomesAndOutcomesYearlyResponse
    }

    private suspend fun groupMonthlyOperations(
        operations: List<Operation>,
        monthsRequired: List<Int>
    ): Pair<MutableList<BarEntry>, MutableList<BarEntry>> {
        val monthlyOperationsMap = operations.groupBy {
            it.date.monthValue
        }.toMutableMap()

        monthsRequired.forEach {
            if (!monthlyOperationsMap.containsKey(it)) {
                monthlyOperationsMap[it] = listOf()
            }
        }
        val monthlyOperationsEntries = monthlyOperationsMap.entries.sortedBy { it.key }

        return getMonthlyOutcomesAndIncomesBarsGrouped(monthlyOperationsEntries)
    }

    private suspend fun groupYearlyOperations(operations: List<Operation>): Pair<MutableList<BarEntry>, MutableList<BarEntry>> {
        val yearlyOperations = operations.groupBy {
            it.date.year
        }.entries.sortedBy { it.key }

        val outcomesBars = mutableListOf<BarEntry>()
        val incomesBars = mutableListOf<BarEntry>()

        yearlyOperations.forEach { (date, operations) ->
            var incomesDecimal: Long = 0
            var outcomesDecimal: Long = 0
            operations.forEach {
                val currencyPrice =
                    currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

                val moneyDecimal =
                    currencyCalculator.calculateMoneyAsDecimal(it.moneyAmount, currencyPrice)

                when (it.category.type) {
                    CategoryType.INCOME -> incomesDecimal += moneyDecimal
                    CategoryType.OUTCOME -> outcomesDecimal += moneyDecimal
                }
            }
            incomesBars.add(
                BarEntry(
                    date.toFloat(),
                    currencyCalculator.convertToFloatAndRound(incomesDecimal).toFloat()
                )
            )
            outcomesBars.add(
                BarEntry(
                    date.toFloat(),
                    currencyCalculator.convertToFloatAndRound(outcomesDecimal).toFloat()
                )
            )
        }

        return Pair(incomesBars, outcomesBars)
    }

    private suspend fun getMonthlyOutcomesAndIncomesBarsGrouped(monthOperations: List<Map.Entry<Int, List<Operation>>>): Pair<MutableList<BarEntry>, MutableList<BarEntry>> {
        val outcomesBars = mutableListOf<BarEntry>()
        val incomesBars = mutableListOf<BarEntry>()

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        var indexToInsert = 0

        monthOperations.forEach { (month, operations) ->
            var incomesDecimal: Long = 0
            var outcomesDecimal: Long = 0

            operations.forEach {
                val currencyPrice =
                    currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)

                val moneyDecimal =
                    currencyCalculator.calculateMoneyAsDecimal(it.moneyAmount, currencyPrice)

                when (it.category.type) {
                    CategoryType.INCOME -> incomesDecimal += moneyDecimal
                    CategoryType.OUTCOME -> outcomesDecimal += moneyDecimal
                }
            }

            if (outcomesBars.size > currentMonth) {
                incomesBars.add(
                    indexToInsert,
                    BarEntry(
                        month.toFloat(),
                        currencyCalculator.convertToFloatAndRound(incomesDecimal).toFloat()
                    )
                )
                outcomesBars.add(
                    indexToInsert,
                    BarEntry(
                        month.toFloat(),
                        currencyCalculator.convertToFloatAndRound(outcomesDecimal).toFloat()
                    )
                )
                indexToInsert++
            } else {
                incomesBars.add(
                    BarEntry(
                        month.toFloat(),
                        currencyCalculator.convertToFloatAndRound(incomesDecimal).toFloat()
                    )
                )
                outcomesBars.add(
                    BarEntry(
                        month.toFloat(),
                        currencyCalculator.convertToFloatAndRound(outcomesDecimal).toFloat()
                    )
                )
            }
        }

        return Pair(incomesBars, outcomesBars)
    }
}