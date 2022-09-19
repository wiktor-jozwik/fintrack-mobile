package com.example.moneytracker.view.ui.utils

import android.util.Log
import org.json.JSONObject
import retrofit2.Response

fun <T> responseErrorHandler(res: Response<T>): T {
    if (res.isSuccessful) {
        return res.body()!!
    } else {
        val errMsg = res.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        } ?: run {
            res.code().toString()
        }
        Log.d("MT", "Error message: $errMsg")
//
//        if (errMsg == "Unauthorized") {
//            Log.d("MT", "Should delete JWT")
//        }

        throw Exception(errMsg)
    }
}