package com.example.moneytracker.viewmodel.utils

import kotlin.math.roundToInt

class CurrencyCalculator {
    private val MONEY_FACTOR = 100
    private val CURRENCY_FACTOR = 10000

    fun calculateMoneyAsDecimal(money: Double, currencyPrice: Double): Long =
        (money * MONEY_FACTOR).toLong() * (currencyPrice * CURRENCY_FACTOR).toLong()

    fun convertToFloatAndRound(money: Long) =
        (money * 100.0 / (CURRENCY_FACTOR * MONEY_FACTOR)).roundToInt() / 100.0
}
