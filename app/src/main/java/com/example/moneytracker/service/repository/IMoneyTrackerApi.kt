package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Operation
import java.time.LocalDateTime

interface IMoneyTrackerApi {
    fun getOperationList(): List<Operation>

    fun getOperationsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Operation>
}