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
import javax.inject.Inject

@HiltViewModel
class CategoriesSplitChartViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyCalculator: CurrencyCalculator
) : ViewModel() {
    suspend fun getSplitOperationByCategories(startDate: LocalDate?, endDate: LocalDate?): Pair<List<String>, List<BarEntry>> {
        var categoriesNames = mutableListOf<String>()
        val categoriesValues = mutableListOf<Double>()
        val categoriesBars = mutableListOf<BarEntry>()

        val operations: List<Operation> = if (startDate != null && endDate != null) {
            operationRepository.getAllOperationsInRanges(startDate, endDate)
        } else {
            operationRepository.getAllOperations()
        }


        val defaultCurrencyName: String = currencyRepository.getUserDefaultCurrency().body()?.name ?: "PLN"

        val categoriesOutcomeGrouped = operations.filter {
            it.category.type == CategoryType.OUTCOME
        }.groupBy {
            it.category
        }

        categoriesOutcomeGrouped.forEach { (category, operations) ->
            var outcome = 0.0
            operations.forEach {
                outcome += currencyRepository.convertCurrency(it.currency.name, defaultCurrencyName, it.moneyAmount, it.date)
            }

            categoriesNames.add(category.name)
            categoriesValues.add(currencyCalculator.roundMoney(outcome))
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

        Pair(sortedCategories.first, categoriesBars)

        return Pair(sortedCategories.first, categoriesBars)
    }
}