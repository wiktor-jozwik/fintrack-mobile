package com.example.fintrack.modules

import com.example.fintrack.service.repository.ft.*
import com.example.fintrack.service.repository.nbp.NbpApi
import com.example.fintrack.service.repository.nbp.NbpRepository
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
    fun provideUserRepository(finTrackApi: FinTrackApi) =
        UserRepository(finTrackApi)

    @Singleton
    @Provides
    fun provideOperationRepository(finTrackApi: FinTrackApi) =
        OperationRepository(finTrackApi)

    @Singleton
    @Provides
    fun provideOperationCategoryRepository(finTrackApi: FinTrackApi) =
        CategoryRepository(finTrackApi)

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        finTrackApi: FinTrackApi,
    ) =
        CurrencyRepository(finTrackApi)

    @Singleton
    @Provides
    fun provideNbpRepository(
        nbpApi: NbpApi,
    ) =
        NbpRepository(nbpApi)
}