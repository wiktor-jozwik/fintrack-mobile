package com.example.moneytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.Operation
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.viewmodel.utils.ExpenseCalculator
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChartPeriodOperationsViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val expenseCalculator: ExpenseCalculator
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
        val fromDate = LocalDate.now(ZoneId.systemDefault()).minusMonths((x - 1).toLong())
            .with(TemporalAdjusters.firstDayOfMonth());
        val toDate = LocalDate.now(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());

        val operations = operationRepository.getAllOperationsInRanges(fromDate, toDate)

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
    }

    private suspend fun getAllTimeOperations(): Pair<List<BarEntry>, List<BarEntry>> {
        val operations = operationRepository.getAllOperations()
        return groupYearlyOperations(operations.sortedByDescending { it.date })
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
            val (incomes, outcomes) = expenseCalculator.calculate(operations)
            incomesBars.add(
                BarEntry(
                    date.toFloat(),
                    incomes.toFloat()
                )
            )
            outcomesBars.add(
                BarEntry(
                    date.toFloat(),
                    outcomes.toFloat()
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

            val (incomes, outcomes) = expenseCalculator.calculate(operations)

            if (outcomesBars.size > currentMonth) {
                incomesBars.add(
                    indexToInsert,
                    BarEntry(
                        month.toFloat(),
                        incomes.toFloat()
                    )
                )
                outcomesBars.add(
                    indexToInsert,
                    BarEntry(
                        month.toFloat(),
                        outcomes.toFloat()
                    )
                )
                indexToInsert++
            } else {
                incomesBars.add(
                    BarEntry(
                        month.toFloat(),
                        incomes.toFloat()
                    )
                )
                outcomesBars.add(
                    BarEntry(
                        month.toFloat(),
                        outcomes.toFloat()
                    )
                )
            }
        }

        return Pair(incomesBars, outcomesBars)
    }
}