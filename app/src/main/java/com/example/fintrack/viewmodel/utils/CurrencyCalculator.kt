package com.example.fintrack.viewmodel.utils

import kotlin.math.roundToInt

class CurrencyCalculator {
    fun roundMoney(money: Double) = (money * 100.0).roundToInt() / 100.0
}
