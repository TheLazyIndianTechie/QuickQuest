package com.meta.quicklauncher.di

import com.meta.quicklauncher.domain.usecase.GetInstalledAppsUseCase
import com.meta.quicklauncher.domain.usecase.SearchAppsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetInstalledAppsUseCase(
        useCase: GetInstalledAppsUseCase
    ): GetInstalledAppsUseCase = useCase

    @Provides
    @Singleton
    fun provideSearchAppsUseCase(
        useCase: SearchAppsUseCase
    ): SearchAppsUseCase = useCase
}