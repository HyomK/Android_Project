package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.firebase.NotificationDTO
import com.likefirst.btos.data.entities.firebase.UserDTO

@Dao
interface NotificationDao {
    @Insert
    fun insert (notification: NotificationDTO)

    @Update
    fun update (notification: NotificationDTO)

    @Delete
    fun delete (notification: NotificationDTO)

}