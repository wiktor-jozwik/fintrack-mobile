package com.example.moneytracker.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.FormatToIsoDateWithSlashes(): String =
    this.format(
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    )

fun LocalDate.FormatToIsoDateWithDashes(): String =
    this.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
