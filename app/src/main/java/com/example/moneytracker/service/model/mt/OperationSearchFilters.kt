package com.example.moneytracker.service.model.mt

import java.time.LocalDate

data class OperationSearchFilters(
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val categoryType: CategoryType?,
    val searchName: String?,
    val includeInternal: Boolean?,
    val operator: String?,
    val moneyAmount: Double?,
)