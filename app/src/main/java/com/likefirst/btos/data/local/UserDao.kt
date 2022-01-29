package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.User


@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT email FROM UserTable")
    fun getEmail(): String?

    @Query("SELECT nickName FROM UserTable")
    fun getNickName(): String?

    @Query("SELECT birth FROM UserTable")
    fun getBirth(): Int?

    @Query("SELECT * FROM UserTable")
    fun getUser(): User


}