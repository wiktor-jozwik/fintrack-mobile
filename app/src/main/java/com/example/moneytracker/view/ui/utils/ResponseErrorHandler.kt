package com.example.moneytracker.view.ui.utils

import android.util.Log
import org.json.JSONObject
import retrofit2.Response

fun <T> responseErrorHandler(res: Response<T>): T {
    if (res.isSuccessful) {
        return res.body()!!
    } else {
        val errMsg = res.errorBody()?.string()?.let {
            Log.d("MT", it.toString())
            JSONObject(it).getString("message")
        } ?: run {
            res.code().toString()
        }

        throw Exception(errMsg)
    }
}