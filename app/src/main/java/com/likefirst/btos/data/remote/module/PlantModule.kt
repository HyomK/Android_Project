package com.likefirst.btos.data.remote.module

import com.likefirst.btos.data.module.DatabaseModule
import com.likefirst.btos.data.remote.plant.dataSource.PlantInfoDataSource
import com.likefirst.btos.data.remote.plant.repository.PlantInfoRepository
import com.likefirst.btos.data.remote.plant.repositoryImpl.PlantInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class PlantModule {

    @Binds
    abstract fun providePlantRepository(impl : PlantInfoRepositoryImpl): PlantInfoRepository

}