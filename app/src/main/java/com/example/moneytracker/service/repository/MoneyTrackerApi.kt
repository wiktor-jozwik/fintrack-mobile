package com.example.moneytracker.service.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moneytracker.service.model.Operation
import com.example.moneytracker.service.model.OperationCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class MoneyTrackerApi: IMoneyTrackerApi {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getOperationList(): List<Operation> {
        val c1 = OperationCategory("Kategoria1")
        val op1 = Operation("Przelew", 1.55, c1, LocalDateTime.now())

        val op2 = Operation("Przelew2", 12.55, c1, LocalDateTime.now())

        val op3 = Operation("Przelew3", 25.66, c1, LocalDateTime.now())

        Log.i("operations", op1.toString())

        return listOf(
            op1, op2, op3
        )
    }
}