package com.example.moneytracker.service.repository.internal

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ServiceInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = sharedPreferences.getString("JWT_AUTH_TOKEN", "")

        if (request.header("No-Authentication") == null) {
            if (token != null && token.isNotEmpty()) {
                val bearerToken = "Bearer $token"

                request = request.newBuilder()
                    .addHeader("Authorization", bearerToken)
                    .build()
            }

        }

        return chain.proceed(request)
    }
}