package com.example.moneytracker.service.repository.mt

import android.util.Log
import com.example.moneytracker.MoneyTrackerApplication
import com.example.moneytracker.service.utils.Constants
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class MoneyTrackerApi @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val cacheInterceptor: CacheInterceptor,
    private val application: MoneyTrackerApplication
) {
    val api: MoneyTrackerApiInterface by lazy {
        Log.d("MT", "retrofit")

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
                    .addNetworkInterceptor(cacheInterceptor)
                    .addInterceptor(authInterceptor)
                    .cache(Cache(File(application.cacheDir, "http-cache"), 10 * 1024 * 1024))
                    .build()
            )
            .build()
            .create(MoneyTrackerApiInterface::class.java)
    }
}