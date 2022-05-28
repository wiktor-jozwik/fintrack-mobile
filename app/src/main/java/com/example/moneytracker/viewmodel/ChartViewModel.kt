package com.example.moneytracker.viewmodel

import android.util.Log
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

    suspend fun getLastXMonthsOperations(num: Int): MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> {
        val incomesAndOutcomesYearlyResponse: MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> = MutableLiveData()

        val fromDate = LocalDate.now(ZoneId.systemDefault()).minusMonths((num - 1).toLong()).with(TemporalAdjusters.firstDayOfMonth());
        val toDate = LocalDate.now(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());

        Log.d("MT", fromDate.toString())
        Log.d("MT", toDate.toString())

        val operations = operationRepository.getAllOperationsInRanges(fromDate, toDate);

        Log.d("MT", operations.toString())

        incomesAndOutcomesYearlyResponse.value = groupMonthlyOperations(operations)

        return incomesAndOutcomesYearlyResponse
    }

    suspend fun getAllTimeOperations(): MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> {
        val incomesAndOutcomesYearlyResponse: MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> = MutableLiveData()

        val operations =  operationRepository.getAllOperations()

        incomesAndOutcomesYearlyResponse.value = groupYearlyOperations(operations)

        return incomesAndOutcomesYearlyResponse
    }

    private fun groupMonthlyOperations(operations: List<Operation>) : Pair<MutableList<BarEntry>,  MutableList<BarEntry>> {
        val monthlyOperations = operations.groupBy {
            it.date.monthValue
        }.entries.sortedBy { it.key }

        return getOutcomesAndIncomesBarsGrouped(monthlyOperations)
    }

    private fun groupYearlyOperations(operations: List<Operation>) : Pair<MutableList<BarEntry>,  MutableList<BarEntry>> {
        val monthlyOperations = operations.groupBy {
            it.date.year
        }.entries.sortedBy { it.key }

        return getOutcomesAndIncomesBarsGrouped(monthlyOperations)
    }

    private fun getOutcomesAndIncomesBarsGrouped(groupedOperations: List<Map.Entry<Int, List<Operation>>>) : Pair<MutableList<BarEntry>,  MutableList<BarEntry>> {
        val outcomesBars = mutableListOf<BarEntry>()
        val incomesBars = mutableListOf<BarEntry>()

        val month = Calendar.getInstance().get(Calendar.MONTH)
        var indexToInsert = 0

        groupedOperations.forEach { (date , operations) ->
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

            if (outcomesBars.size > month) {
                outcomesBars.add(indexToInsert, BarEntry(date.toFloat(), roundMoney(outcomes).toFloat()))
                incomesBars.add(indexToInsert, BarEntry(date.toFloat(), roundMoney(incomes).toFloat()))
                indexToInsert += 1
            } else {
                outcomesBars.add(BarEntry(date.toFloat(), roundMoney(outcomes).toFloat()))
                incomesBars.add(BarEntry(date.toFloat(), roundMoney(incomes).toFloat()))
            }
        }

        return Pair(incomesBars, outcomesBars)
    }

    private fun roundMoney(money: Double): Double {
        return (money * 100.0).roundToInt() / 100.0
    }
}