package com.likefirst.btos.utils

import androidx.room.*

@Dao
interface PlantDao {
    @Insert
    fun insert(plant:PlantItem)

    @Update
    fun update(plant:PlantItem)

    @Delete
    fun delete(plant:PlantItem)

    @Query("SELECT * FROM PlantItemTable")
    fun getPlants(): List<PlantItem>

    @Query("SELECT * FROM PlantItemTable where plantIdx = :id")
    fun getPlant(id: Int): PlantItem?

//
//    @Update(entity = Plant::class)
//    fun update(obj: PlantUpdate)




   class  PlantUpdate {
        var plantIdx : Int = 0
        var plantName : String=""
    }


}