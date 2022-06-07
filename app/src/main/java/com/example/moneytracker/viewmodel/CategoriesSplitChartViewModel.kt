package com.example.moneytracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneytracker.service.model.mt.CategoryType
import com.example.moneytracker.service.repository.mt.CurrencyRepository
import com.example.moneytracker.service.repository.mt.OperationRepository
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
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
        val categoriesValues = mutableListOf<Double>()
        val categoriesBars = mutableListOf<BarEntry>()

        val operations = operationRepository.getAllOperations()

        val categoriesOutcomeGrouped = operations.filter {
            it.category.type == CategoryType.OUTCOME
        }.groupBy {
            it.category
        }

        categoriesOutcomeGrouped.forEach { (category, operations) ->
            var outcome = 0.0
            operations.forEach {
                val currencyPrice =
                    currencyRepository.getPriceOfCurrencyAtDay(it.currency.name, it.date)
                outcome += it.moneyAmount * currencyPrice
            }
            categoriesNames.add(category.name)
            categoriesValues.add(outcome)
        }

        val maxCharsOfCategory = 10
        categoriesNames = categoriesNames.map {
            if (it.length > maxCharsOfCategory) {
                it.substring(0, maxCharsOfCategory) + ".."
            } else {
                it.substring(0, maxCharsOfCategory.coerceAtMost(it.length))
            }
        }.toMutableList()

        val sortedCategories = categoriesNames.zip(categoriesValues).sortedByDescending {
            it.second
        }.unzip()

        sortedCategories.second.forEachIndexed { index, value ->
            categoriesBars.add(BarEntry(index.toFloat(), value.toFloat()))
        }

        outcomesCategoriesSplit.value = Pair(sortedCategories.first, categoriesBars)

        return outcomesCategoriesSplit
    }
}