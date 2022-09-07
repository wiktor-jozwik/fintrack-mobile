package com.example.moneytracker.service.repository.exchangerate

import com.example.moneytracker.service.utils.Constants
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangerateApi {
    val api: ExchangerateInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(Constants.EXCHANGERATE_API_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(OkHttpProfilerInterceptor())
                    .build()
            )
            .build()
            .create(ExchangerateInterface::class.java)
    }
}