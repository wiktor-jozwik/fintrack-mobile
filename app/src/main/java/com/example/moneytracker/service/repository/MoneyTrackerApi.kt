package com.example.moneytracker.service.repository

import com.example.moneytracker.service.model.CurrencyEnum
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import java.time.LocalDateTime

class MoneyTrackerApi: IMoneyTrackerApi {
    private fun fetchOperations(): List<Operation> {
        val c1 = OperationCategory("Kategoria1")
        val op1 = Operation("Przelew", -10.55, c1, LocalDateTime.parse("2022-01-06T21:30:10"), CurrencyEnum.EUR)

        val op2 = Operation("Przelew2", 50.56, c1, LocalDateTime.parse("2022-03-06T14:30:55"), CurrencyEnum.USD)

        val op3 = Operation("Przelew3", 25.66, c1, LocalDateTime.parse("2021-12-06T16:30:33"))

        val op4 = Operation("Przelew2", 68.29, c1, LocalDateTime.parse("2022-04-06T14:30:55"))

        val op5 = Operation("Przelew2", -55.29, c1, LocalDateTime.parse("2022-02-04T14:30:55"))

        val op6 = Operation("Przelew2", 72.29, c1, LocalDateTime.parse("2019-04-06T14:30:55"))

        return listOf(
            op1, op2, op3, op4, op5, op6
        )
    }

    override fun getOperationList(): List<Operation> {
        return fetchOperations()
    }

    override fun getOperationListInRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Operation> {
        val ops = getOperationList()

        return ops.filter {
            it.date > startDate && it.date <= endDate
        }
    }
}