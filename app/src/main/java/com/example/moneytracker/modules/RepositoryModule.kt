package com.example.moneytracker.modules

import com.example.moneytracker.service.repository.mt.*
import com.example.moneytracker.service.repository.nbp.NbpApi
import com.example.moneytracker.service.repository.nbp.NbpRepository
import com.example.moneytracker.viewmodel.utils.CurrencyCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(moneyTrackerApi: MoneyTrackerApi) =
        UserRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideOperationRepository(moneyTrackerApi: MoneyTrackerApi) =
        OperationRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideOperationCategoryRepository(moneyTrackerApi: MoneyTrackerApi) =
        CategoryRepository(moneyTrackerApi)

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        moneyTrackerApi: MoneyTrackerApi,
        nbpApi: NbpApi,
    ) =
        CurrencyRepository(moneyTrackerApi, nbpApi)

    @Singleton
    @Provides
    fun provideNbpRepository(
        nbpApi: NbpApi,
    ) =
        NbpRepository(nbpApi)
}