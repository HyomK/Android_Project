package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.entities.firebase.UserDTO

@Dao
interface FCMDao {
    @Insert
    fun insert(user: UserDTO)

    @Update
    fun update(user: UserDTO)

    @Delete
    fun delete(user: UserDTO)

    @Query("SELECT * FROM FCMTable")
    fun getData(): UserDTO
}