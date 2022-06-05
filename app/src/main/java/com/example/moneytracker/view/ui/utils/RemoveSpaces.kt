package com.example.moneytracker.view.ui.utils

fun String.removeSpaces() =
    replace("\\s+".toRegex(), " ")
