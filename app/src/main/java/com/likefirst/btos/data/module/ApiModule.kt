package com.likefirst.btos.data.module

import com.likefirst.btos.data.remote.plant.dataSource.PlantInfoDataSource
import com.likefirst.btos.data.remote.plant.repository.PlantInfoRepository
import com.likefirst.btos.data.remote.plant.repository.PlantRepository
import com.likefirst.btos.data.remote.plant.repositoryImpl.PlantInfoRepositoryImpl
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
    fun provideApiService(): ApiInterface {
        return ApiInterface.create()
    }



}
