package com.likefirst.btos.data.remote.posting.module

import com.likefirst.btos.data.remote.plant.repositoryImpl.PlantInfoRepositoryImpl
import com.likefirst.btos.data.remote.posting.repository.HistorySearchRepository
import com.likefirst.btos.data.remote.posting.repositoryImpl.HistorySearchRepositoryImpl
import com.likefirst.btos.domain.repository.PlantInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class HistoryModule {
    @Binds
    abstract fun provideHistorySearchRepository(impl : HistorySearchRepositoryImpl): HistorySearchRepository
}