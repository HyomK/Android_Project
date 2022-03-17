package com.likefirst.btos.data.remote.plant.repository
import androidx.lifecycle.LiveData
import android.app.Application
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantDao
import com.likefirst.btos.data.local.PlantDatabase


class PlantRepository(application: Application) {
    private var mPlantDao: PlantDao = PlantDatabase.getInstance(application)!!.plantDao()
    private var mPlantList: LiveData<List<Plant>> = mPlantDao.getPlants()


    init{
        val db= PlantDatabase.getInstance(application)!!
        mPlantDao = db.plantDao()
        mPlantList =  mPlantDao.getPlants()

    }

    fun getPlantList () :LiveData<List<Plant>> {
        return mPlantDao.getPlants()
    }

   fun insert(plant : Plant) {
       mPlantDao.insert(plant)
    }

    fun getPlant(idx: Int) : Plant{
        return mPlantDao.getPlant(idx)
    }

    fun getCurrentPlant():LiveData<Plant>{
        return mPlantDao.getCurrentPlant()
    }

    fun getSelectedPlant():Plant{
        return mPlantDao.getSelectedPlant("selected")!!
    }

    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        mPlantDao.setPlantInit(idx, status, level, isOwn)
    }

    fun setPlantStatus(idx: Int, status:String){
        mPlantDao.setPlantStatus(idx,status)
    }
}