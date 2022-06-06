package com.example.moneytracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.CategoryType
import com.example.moneytracker.service.repository.internal.CurrencyRepository
import com.example.moneytracker.service.repository.internal.OperationRepository
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Integer.max
import java.lang.Math.min
import javax.inject.Inject

@HiltViewModel
class CategoriesSplitChartViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    suspend fun getSplitOperationByCategories(): MutableLiveData<Pair<List<String>, List<BarEntry>>> {
        val outcomesCategoriesSplit: MutableLiveData<Pair<List<String>, List<BarEntry>>> =
            MutableLiveData()

        var categoriesNames = mutableListOf<String>()
        val categoriesBars = mutableListOf<BarEntry>()

        val operations = operationRepository.getAllOperations()

        val categoriesOutcomeGrouped = operations.filter {
            it.category.type == CategoryType.OUTCOME
        } .groupBy {
            it.category
        }

        var index = 0
        categoriesOutcomeGrouped.forEach { (category, operations) ->
            var outcome = 0.0
            operations.forEach {
                outcome += it.moneyAmount
            }
            categoriesNames.add(category.name)
            categoriesBars.add(BarEntry(index.toFloat(), outcome.toFloat()))
            index++
        }

        val maxCharsOfCategory = 10
        categoriesNames = categoriesNames.map {
            if (it.length > maxCharsOfCategory) {
                it.substring(0, maxCharsOfCategory) + ".."
            } else {
                it.substring(0, maxCharsOfCategory.coerceAtMost(it.length))
            }
        }.toMutableList()

        outcomesCategoriesSplit.value = Pair(
            categoriesNames,
            categoriesBars
        )

        return outcomesCategoriesSplit
    }
}