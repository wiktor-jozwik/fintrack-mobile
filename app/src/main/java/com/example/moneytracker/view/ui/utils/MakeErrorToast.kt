package com.example.moneytracker.view.ui.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.moneytracker.R


fun makeErrorToast(context: Context, message: String?, yOffset: Int = 0) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    val view = toast.view
    view!!.setBackgroundColor(Color.TRANSPARENT)
    val text = view.findViewById<View>(android.R.id.message) as TextView
    text.setShadowLayer(
        0f,
        0f,
        0f,
        Color.TRANSPARENT
    )
    text.setTextColor(ContextCompat.getColor(context, R.color.main_red))
    text.textSize = 16f
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, yOffset)
    toast.show()
}