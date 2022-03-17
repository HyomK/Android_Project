package com.likefirst.btos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.likefirst.btos.data.module.Plant

@Database(
    entities = [
        Plant::class
    ], version = 1
)
//companion object로 싱글톤 db 만들어서 바로 불러서 썼던 것과 다르게,
//hilt에서는 추상클래스와 추상메소드로 만들고 module에서 구현해 준다음
//실제 사용하는 곳에서는 inject 해서 사용한다.
abstract class PlantInfoDatabase : RoomDatabase() {
    abstract fun plantInfoDao(): PlantInfoDao
}
