package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.Plant


@Dao
interface PlantDao {
    @Insert
    fun insert(plant: Plant)

    @Update
    fun update(plant: Plant)

    @Delete
    fun delete(plant:Plant)

    @Query("SELECT * FROM PlantTable")
    fun getPlants(): List<Plant>

    @Query("SELECT * FROM PlantTable where plantIdx = :id")
    fun getPlant(id: Int): Plant?

    @Query("UPDATE PlantTable SET plantStatus= :status WHERE plantIdx = :id")
    fun setPlantStatus(id: Int, status : String)

    @Query("SELECT * FROM PlantTable where plantStatus = :status")
    fun getSelectedPlant(status:String):Plant?



}