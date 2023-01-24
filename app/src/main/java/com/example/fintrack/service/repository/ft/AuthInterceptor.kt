package com.example.fintrack.service.repository.ft

import android.content.SharedPreferences
import com.example.fintrack.service.model.ft.JwtResponse
import com.example.fintrack.service.utils.Constants.Companion.API_URL
import com.google.gson.Gson
import okhttp3.*
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferences.getString("JWT_ACCESS_TOKEN", "")
        val refreshToken = sharedPreferences.getString("JWT_REFRESH_TOKEN", "")

        var originalRequest = chain.request()
        if (!accessToken.isNullOrBlank()) {
            originalRequest = originalRequest.newBuilder().addHeaders(accessToken).build()
        }

        val originalResponse = chain.proceedDeletingTokenOnError(originalRequest)

        if (originalResponse.isSuccessful) {
            return originalResponse
        }
        originalResponse.close()

        if (!refreshToken.isNullOrBlank()) {
            val refreshRequest =
                originalRequest
                    .newBuilder()
                    .post(
                        RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            ""
                        )
                    )
                    .url("${API_URL}auth/refresh")
                    .addHeaders(refreshToken)
                    .build()
            val refreshResponse = chain.proceedDeletingTokenOnError(refreshRequest)

            if (refreshResponse.isSuccessful) {
                val jwt: JwtResponse =
                    Gson().fromJson(refreshResponse.body()?.string(), JwtResponse::class.java)

                with(sharedPreferences.edit()) {
                    putString("JWT_ACCESS_TOKEN", jwt.jwtAccessToken)
                    putString("JWT_REFRESH_TOKEN", jwt.jwtRefreshToken)
                    apply()
                }

                val newCall = originalRequest.newBuilder().addHeaders(jwt.jwtAccessToken).build()

                return chain.proceedDeletingTokenOnError(newCall)
            }
            refreshResponse.close()
        }

        val endingRequest = chain.proceedDeletingTokenOnError(originalRequest)
        with(sharedPreferences.edit()) {
            putString("JWT_REFRESH_TOKEN", "")
            apply()
        }
        return endingRequest
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (response.code() == 401) {
            with(sharedPreferences.edit()) {
                putString("JWT_ACCESS_TOKEN", "")
                apply()
            }
        }
        return response
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply { header("Authorization", "Bearer $token") }
}