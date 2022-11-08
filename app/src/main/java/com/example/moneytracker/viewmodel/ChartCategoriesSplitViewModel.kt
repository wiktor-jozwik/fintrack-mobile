package com.example.moneytracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.example.moneytracker.view.ui.utils.cutText
import com.example.moneytracker.viewmodel.utils.ExpenseCalculator
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ChartCategoriesSplitViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val expenseCalculator: ExpenseCalculator,
) : ViewModel() {
    suspend fun getSplitOperationByCategories(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Pair<List<String>, List<BarEntry>> {
        var categoriesNames = mutableListOf<String>()
        val categoriesValues = mutableListOf<Double>()
        val categoriesBars = mutableListOf<BarEntry>()

        val operations = operationRepository.getAllOperationsInDefaultCurrency(startDate, endDate)

        val categoriesOutcomeGrouped = operations.filter {
            it.category.type == CategoryType.OUTCOME && !it.category.isInternal
        }.groupBy {
            it.category
        }

        categoriesOutcomeGrouped.forEach { (category, operations) ->
            val (_, outcomes) = expenseCalculator.calculate(operations)
            categoriesNames.add(category.name)
            categoriesValues.add(outcomes)
        }


        val minNumberOfBarsPlaceholders = 20
        while (categoriesNames.size < minNumberOfBarsPlaceholders && categoriesValues.size < minNumberOfBarsPlaceholders) {
            categoriesNames.add("")
            categoriesValues.add(0.0)
        }

        categoriesNames = categoriesNames.map {
            it.cutText(10)
        }.toMutableList()

        val sortedCategories = categoriesNames.zip(categoriesValues).sortedByDescending {
            it.second
        }.unzip()

        sortedCategories.second.forEachIndexed { index, value ->
            categoriesBars.add(BarEntry(index.toFloat(), value.toFloat()))
        }

        Log.d("MT", Pair(sortedCategories.first, categoriesBars).toString())

        return Pair(sortedCategories.first, categoriesBars)
    }
}