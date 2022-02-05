package com.likefirst.btos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.firebase.UserDTO

@Database(entities = [UserDTO::class], version = 3)
abstract class FCMDatabase : RoomDatabase()  {
    abstract fun fcmDao() : FCMDao
    companion object{
        private  var instance: FCMDatabase?=null

        @Synchronized
        fun getInstance(context: Context): FCMDatabase? {
            if (instance == null) {
                synchronized(FCMDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FCMDatabase::class.java,
                        "fcm-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}