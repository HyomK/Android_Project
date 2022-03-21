package com.likefirst.btos.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.likefirst.btos.data.entities.Plant


@Dao
interface PlantInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(plant: Plant)

    @Update
    fun update(plant: Plant)

    @Delete
    fun delete(plant: Plant)

    @Query("SELECT * FROM PlantInfoTable")
    fun getPlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM PlantInfoTable where plantIdx = :id")
    fun getPlant(id: Int): Plant

    @Query("SELECT * FROM PlantInfoTable where plantStatus = :status")
    fun getCurrentPlant(status: String = "selected"): LiveData<Plant>

    @Query("UPDATE PlantInfoTable SET plantStatus= :status WHERE plantIdx = :id")
    fun setPlantStatus(id: Int, status: String)

    @Query("SELECT * FROM PlantInfoTable where plantStatus = :status")
    fun getSelectedPlant(status: String = "selected"): Plant?

    @Query("UPDATE PlantInfoTable SET isOwn = :isOwn WHERE plantIdx = :plantIdx")
    fun setPlantIsOwn(isOwn: Boolean = true, plantIdx: Int)

    @Query("UPDATE PlantInfoTable SET  plantStatus = :status ,currentLevel = :curLevel ,isOwn = :isOwn WHERE plantIdx = :plantIdx")
    fun setPlantInit(plantIdx: Int, status: String, curLevel: Int, isOwn: Boolean)

}