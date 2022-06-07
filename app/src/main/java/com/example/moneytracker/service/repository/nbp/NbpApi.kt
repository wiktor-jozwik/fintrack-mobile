package com.example.moneytracker.service.repository.nbp

import com.example.moneytracker.service.utils.Constants
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NbpApi {
    val api: NbpApiInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(Constants.NBP_API_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(OkHttpProfilerInterceptor())
                    .build()
            )
            .build()
            .create(NbpApiInterface::class.java)
    }
}