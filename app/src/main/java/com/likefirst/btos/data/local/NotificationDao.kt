package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.firebase.NotificationDTO


@Dao
interface NotificationDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert (notification: NotificationDTO)

    @Update
    fun update (notification: NotificationDTO)

    @Delete
    fun delete (notification: NotificationDTO)

    @Query("DELETE  FROM NotificationTable WHERE alarmIdx=:Idx")
    fun deleteNotifications(Idx: Int)

    @Query("SELECT * FROM NotificationTable ORDER BY createAt DESC")
    fun getNotifications(): List<NotificationDTO>

    @Query("SELECT COUNT(*) FROM NotificationTable")
    fun itemCount(): Int

    @Query("SELECT * FROM NotificationTable WHERE alarmIdx=:Idx ")
    fun getNotification(Idx : Int):NotificationDTO

    @Query("UPDATE NotificationTable SET isChecked=:status WHERE alarmIdx=:Idx")
    fun setIsChecked(Idx: Int , status:Boolean = true)

    @Query("SELECT * FROM NotificationTable WHERE isChecked=:status ORDER BY createAt DESC")
    fun getUnreadNotifications(status:Boolean=false): List<NotificationDTO>
}