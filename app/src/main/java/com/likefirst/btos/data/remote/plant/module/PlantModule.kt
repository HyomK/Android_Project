package com.likefirst.btos.data.remote.plant.module

import com.likefirst.btos.domain.repository.PlantInfoRepository
import com.likefirst.btos.data.remote.plant.repositoryImpl.PlantInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class PlantModule {
    @Binds
    abstract fun providePlantRepository(impl : PlantInfoRepositoryImpl): PlantInfoRepository

}