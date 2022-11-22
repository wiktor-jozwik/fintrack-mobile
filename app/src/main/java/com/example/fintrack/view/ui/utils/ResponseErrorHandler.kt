package com.example.fintrack.view.ui.utils

import android.util.Log
import org.json.JSONObject
import retrofit2.Response

fun <T> responseErrorHandler(res: Response<T>): T {
    if (res.isSuccessful) {
        return res.body()!!
    } else {
        var errMsg = res.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        } ?: run {
            res.code().toString()
        }

        Log.d("MT", errMsg)

        if (errMsg == "Unauthorized") {
            errMsg = "Unauthorized, please re-login"
        }

        throw Exception(errMsg)
    }
}