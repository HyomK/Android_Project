package com.likefirst.btos.data.module

import com.likefirst.btos.data.remote.plant.PlantApiInterface
import com.likefirst.btos.data.remote.posting.HistoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun providePlantApiService(): PlantApiInterface {
        return PlantApiInterface.create()
    }
    @Singleton
    @Provides
    fun provideHistorySearchApiService():HistoryApi{
        return HistoryApi.create()
    }
}
