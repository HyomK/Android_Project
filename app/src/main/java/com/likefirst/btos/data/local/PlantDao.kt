package com.likefirst.btos.data.local

import androidx.room.*
import com.likefirst.btos.data.entities.PlantItem

@Dao
interface PlantDao {
    @Insert
    fun insert(plant: PlantItem)

    @Update
    fun update(plant: PlantItem)

    @Delete
    fun delete(plant: PlantItem)

    @Query("SELECT * FROM PlantItemTable")
    fun getPlants(): List<PlantItem>

    @Query("SELECT * FROM PlantItemTable where plantIdx = :id")
    fun getPlant(id: Int): PlantItem?


   class  PlantUpdate {
        var plantIdx : Int = 0
        var plantName : String=""
    }
}