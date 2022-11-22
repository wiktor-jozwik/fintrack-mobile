package com.example.fintrack.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formatToIsoDateWithSlashes(): String =
    this.format(
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    )

fun LocalDate.formatToIsoDateWithDashes(): String =
    this.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
