package com.likefirst.btos.data.module

import android.content.Context
import androidx.room.Room
import com.likefirst.btos.data.local.PlantInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class) //액티비티가 혹은 프래그먼트가 살아있는 동안에는 같은 hilt 인스턴스을 쓰겠다. 를 지정해주는 곳
//ApplicationComponent 는 singleton 범위로써, application이 생성되고 죽을때까지 계속 똑같은 hilt 인스턴스를 반환한다.
//리소스가 많이 소비되기 때문에 db를 싱글톤으로 만들으라는 room 규칙에 들어맞음.
@Module // (UserDataBase) 추상 클래스는 바로 inject 해서 쓸 수 없다. hilt가 추상 클래스가 어디에 구현되어 있는지 모르기 때문
//따라서 Module을 만들어서 구현해야 hilt가 구현부를 찾아 갈 수 있다.
//hilt야 Database 여기 모듈에 구현되어있어 라고 말하는 너낌
class DatabaseModule {
    @Provides //Moudle을 구현하는 방법에는 Provides와 Bidns 두가지가 있다. 외부라이브러리에는 Binds를 못쓴다.
    @Singleton //module을 싱글톤으로 만들겠다.
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            PlantInfoDatabase::class.java,
            "plantInfo_db"
        ).fallbackToDestructiveMigration()
            .build()
    @Provides
    fun providePlantInfoDao(appDatabase: PlantInfoDatabase) = appDatabase.plantInfoDao()
}