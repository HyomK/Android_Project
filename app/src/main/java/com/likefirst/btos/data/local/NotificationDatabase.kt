package com.likefirst.btos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.likefirst.btos.data.entities.firebase.NotificationDTO

@Database(entities = [NotificationDTO::class], version = 3)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun NotificationDao(): NotificationDao
    companion object{
        private  var instance: NotificationDatabase?=null

        @Synchronized
        fun getInstance(context: Context): NotificationDatabase? {
            if (instance == null) {
                synchronized( NotificationDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotificationDatabase::class.java,
                        "notice-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}

