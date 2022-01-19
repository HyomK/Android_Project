package com.likefirst.btos.utils


import android.content.res.AssetManager
import android.system.Os.open
import com.google.gson.Gson
import com.likefirst.btos.ApplicationClass
import java.io.FileNotFoundException
import java.io.InputStream


class PlantService {


    private lateinit var plantView : PlantListView
    fun setPlantListView(plantView: PlantListView){
        this.plantView=plantView
    }

    fun getPlants(){

        //dummyDat

    }


}