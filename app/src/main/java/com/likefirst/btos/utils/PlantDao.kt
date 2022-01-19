package com.likefirst.btos.utils

import androidx.room.*

@Dao
interface PlantDao {
    @Insert
    fun insert(plant:Plant)

    @Update
    fun update(plant:Plant)

    @Delete
    fun delete(plant:Plant)

    @Query("SELECT * FROM PlantTable")
    fun getPlants(): List<Plant>

    @Query("SELECT * FROM PlantTable where plantIdx = :id")
    fun getPlant(id: Int): Plant?


}