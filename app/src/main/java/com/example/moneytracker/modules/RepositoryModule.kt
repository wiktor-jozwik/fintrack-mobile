package com.example.moneytracker.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.moneytracker.service.repository.internal.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(moneyTrackerApi: MoneyTrackerApi) = UserRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideOperationRepository(moneyTrackerApi: MoneyTrackerApi) = OperationRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideOperationCategoryRepository(moneyTrackerApi: MoneyTrackerApi) = CategoryRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideCurrencyRepository(moneyTrackerApi: MoneyTrackerApi) = CurrencyRepository(moneyTrackerApi)
}