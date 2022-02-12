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
    fun getSelectedPlant(status:String="selected"):Plant?

    @Query("UPDATE PlantTable SET isOwn = :isOwn WHERE plantIdx = :plantIdx")
    fun setPlantIsOwn(isOwn:Boolean=true, plantIdx:Int)

    @Query("UPDATE PlantTable SET  plantStatus = :status ,currentLevel = :curLevel ,isOwn = :isOwn WHERE plantIdx = :plantIdx")
    fun setPlantInit(plantIdx:Int, status:String , curLevel : Int, isOwn:Boolean )

}