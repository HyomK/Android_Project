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

    @Query("SELECT * FROM NotificationTable ORDER BY detailIdx DESC")
    fun getNotifications(): List<NotificationDTO>

    @Query("SELECT COUNT(*) FROM NotificationTable")
    fun itemCount(): Int

    @Query("SELECT * FROM NotificationTable WHERE timestamp=:time and detailIdx =:detailIdx and type = :type ")
    fun getNotification(time:String, detailIdx : Int, type: String):NotificationDTO

    @Query("SELECT * FROM NotificationTable WHERE type= :type")
    fun getNotificationsByType(type: String): List<NotificationDTO>

    @Query("UPDATE NotificationTable SET isRead=:status WHERE type=:type and detailIdx=:Idx")
    fun setIsRead(type:String, Idx: Int , status:Boolean)

    @Query("SELECT * FROM NotificationTable WHERE isRead=:status ORDER BY detailIdx DESC")
    fun getUnreadNotifications(status:Boolean=false): List<NotificationDTO>
}