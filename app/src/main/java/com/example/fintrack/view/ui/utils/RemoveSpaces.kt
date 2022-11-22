package com.example.fintrack.view.ui.utils

fun String.removeSpaces() =
    replace("\\s+".toRegex(), " ")
