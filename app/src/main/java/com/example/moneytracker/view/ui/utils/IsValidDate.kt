package com.example.moneytracker.view.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeParseException

fun isValidDate(string: String): Boolean {
    try {
        LocalDate.parse(string)
    } catch (e: DateTimeParseException) {
        return false;
    }
    return true;
}
