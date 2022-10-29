package com.example.moneytracker.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.moneytracker.service.repository.mt.MoneyTrackerApi
import com.example.moneytracker.service.repository.mt.AuthInterceptor
import com.example.moneytracker.service.repository.nbp.NbpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): AuthInterceptor =
        AuthInterceptor(sharedPreferences)

    @Singleton
    @Provides
    fun provideMoneyTrackerApi(authInterceptor: AuthInterceptor): MoneyTrackerApi =
        MoneyTrackerApi(authInterceptor)

    @Singleton
    @Provides
    fun provideNbpApi(): NbpApi =
        NbpApi()
}