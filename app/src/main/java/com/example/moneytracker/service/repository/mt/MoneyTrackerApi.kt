package com.example.moneytracker.service.repository.mt

import com.example.moneytracker.service.utils.Constants
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Inject

class MoneyTrackerApi @Inject constructor(
    private val authInterceptor: AuthInterceptor,
) {
    val api: MoneyTrackerApiInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapter(
                        LocalDate::class.java,
                        JsonDeserializer { json, _, _ ->
                            LocalDate.parse(
                                json.asJsonPrimitive.asString
                            )
                        }).create(),
                )
            )
            .baseUrl(Constants.MONEY_TRACKER_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(OkHttpProfilerInterceptor())
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()
            .create(MoneyTrackerApiInterface::class.java)
    }
}