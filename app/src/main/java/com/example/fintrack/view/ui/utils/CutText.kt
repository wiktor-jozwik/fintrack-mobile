package com.example.fintrack.view.ui.utils

fun String.cutText(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength) + ".."
    } else {
        this.substring(0, maxLength.coerceAtMost(this.length))
    }
}
