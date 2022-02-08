package com.likefirst.btos.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.likefirst.btos.data.entities.User


@Dao
interface UserDao {
    @Insert (onConflict = REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT userIdx FROM UserTable")
    fun getUserIdx(): Int

    @Query("SELECT email FROM UserTable")
    fun getEmail(): String?

    @Query("SELECT nickName FROM UserTable")
    fun getNickName(): String?

    @Query("SELECT recOthers FROM UserTable")
    fun getRecOthers(): Boolean?

    @Query("SELECT recSimilarAge FROM UserTable")
    fun getRecSimilarAge(): Boolean?

    @Query("SELECT fontIdx FROM UserTable")
    fun getFontIdx(): Int?

    @Query("SELECT pushAlarm FROM UserTable")
    fun getPushAlarm(): Boolean?

    @Query("SELECT * FROM UserTable")
    fun getUser(): User

    @Query("DELETE FROM UserTable")
    fun deleteAll()

    @Query("SELECT isSad FROM UserTable")
    fun getIsSad() : Boolean

    @Query("UPDATE UserTable SET isSad = :isSad")
    fun updateIsSad(isSad : Boolean)

    @Query("UPDATE UserTable SET nickName = :nickName")
    fun updateNickName(nickName : String)

    @Query("UPDATE UserTable SET birth = :birth")
    fun updateBirth(birth : Int)

    @Query("UPDATE UserTable SET recOthers = :recOthers")
    fun updateRecOthers(recOthers: Boolean)

    @Query("UPDATE UserTable SET recSimilarAge = :recSimilarAge")
    fun updateRecSimilarAge(recSimilarAge: Boolean)

    @Query("UPDATE UserTable SET fontIdx = :fontIdx")
    fun updateFontIdx(fontIdx: Int)

    @Query("UPDATE UserTable SET pushAlarm = :pushAlarm")
    fun updatePushAlarm(pushAlarm: Boolean)

}