package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.Operation

interface IMoneyTrackerApi {
    fun getOperationList(): List<Operation>
}