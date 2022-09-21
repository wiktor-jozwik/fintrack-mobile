package com.example.moneytracker.view.ui.utils

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

        if (res.code() == 401) {
            errMsg = "Unauthorized, please re-login"
        }

        throw Exception(errMsg)
    }
}