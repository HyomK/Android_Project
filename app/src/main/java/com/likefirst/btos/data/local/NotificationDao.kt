package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.Plant
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

    @Query("SELECT * FROM NotificationTable")
    fun getNotifications(): List<NotificationDTO>

    @Query("SELECT * FROM NotificationTable WHERE type= :type")
    fun getNotificationsByType(type: String): List<NotificationDTO>
}