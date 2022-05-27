package com.example.moneytracker.service.repository

import com.example.moneytracker.service.utils.Constants
import com.google.gson.*
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.ZonedDateTime

object MoneyTrackerApi {
    val api: MoneyTrackerApiInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapter(
                        LocalDateTime::class.java,
                        JsonDeserializer { json, _, _ ->
                            ZonedDateTime.parse(
                                json.asJsonPrimitive.asString
                            ).toLocalDateTime()
                        }).create(),
                )
            )
            .baseUrl(Constants.MONEY_TRACKER_API_URL)
            .client(OkHttpClient.Builder().addInterceptor(OkHttpProfilerInterceptor()).build())
            .build()
            .create(MoneyTrackerApiInterface::class.java)
    }
}