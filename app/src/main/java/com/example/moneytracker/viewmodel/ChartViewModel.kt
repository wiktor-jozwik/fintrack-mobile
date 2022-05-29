package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.CategoryType
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.repository.CurrencyRepository
import com.example.moneytracker.service.repository.OperationRepository
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class ChartViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    suspend fun getLastXMonthsOperations(x: Int): MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> {
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

        incomesAndOutcomesYearlyResponse.value =
            groupMonthlyOperations(operations, monthsRequired.sortedBy { it })

        return incomesAndOutcomesYearlyResponse
    }

    suspend fun getAllTimeOperations(): MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> {
        val incomesAndOutcomesYearlyResponse: MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> =
            MutableLiveData()

        val operations = operationRepository.getAllOperations()
        incomesAndOutcomesYearlyResponse.value = groupYearlyOperations(operations)

        return incomesAndOutcomesYearlyResponse
    }

    private fun groupMonthlyOperations(
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

    private fun groupYearlyOperations(operations: List<Operation>): Pair<MutableList<BarEntry>, MutableList<BarEntry>> {
        val yearlyOperations = operations.groupBy {
            it.date.year
        }.entries.sortedBy { it.key }

        val outcomesBars = mutableListOf<BarEntry>()
        val incomesBars = mutableListOf<BarEntry>()

        yearlyOperations.forEach { (date, operations) ->
            var incomes = 0.0
            var outcomes = 0.0
            operations.forEach { op ->
                val currencyPrice =
                    currencyRepository.getPriceOfCurrencyAtDay(op.currency.name, op.date)

                if (op.category.type == CategoryType.INCOME) {
                    incomes += op.moneyAmount * currencyPrice
                } else if (op.category.type == CategoryType.OUTCOME) {
                    outcomes += op.moneyAmount * currencyPrice
                }
            }
            outcomesBars.add(BarEntry(date.toFloat(), roundMoney(outcomes).toFloat()))
            incomesBars.add(BarEntry(date.toFloat(), roundMoney(incomes).toFloat()))
        }

        return Pair(incomesBars, outcomesBars)
    }

    private fun getMonthlyOutcomesAndIncomesBarsGrouped(monthOperations: List<Map.Entry<Int, List<Operation>>>): Pair<MutableList<BarEntry>, MutableList<BarEntry>> {
        val outcomesBars = mutableListOf<BarEntry>()
        val incomesBars = mutableListOf<BarEntry>()

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        var indexToInsert = 0

        monthOperations.forEach { (month, operations) ->
            var incomes = 0.0
            var outcomes = 0.0

            operations.forEach { op ->
                val currencyPrice =
                    currencyRepository.getPriceOfCurrencyAtDay(op.currency.name, op.date)

                if (op.category.type == CategoryType.INCOME) {
                    incomes += op.moneyAmount * currencyPrice
                } else if (op.category.type == CategoryType.OUTCOME) {
                    outcomes += op.moneyAmount * currencyPrice
                }
            }

            if (outcomesBars.size > currentMonth) {
                outcomesBars.add(
                    indexToInsert,
                    BarEntry(month.toFloat(), roundMoney(outcomes).toFloat())
                )
                incomesBars.add(
                    indexToInsert,
                    BarEntry(month.toFloat(), roundMoney(incomes).toFloat())
                )
                indexToInsert++
            } else {
                outcomesBars.add(BarEntry(month.toFloat(), roundMoney(outcomes).toFloat()))
                incomesBars.add(BarEntry(month.toFloat(), roundMoney(incomes).toFloat()))
            }
        }

        return Pair(incomesBars, outcomesBars)
    }

    private fun roundMoney(money: Double): Double {
        return (money * 100.0).roundToInt() / 100.0
    }
}