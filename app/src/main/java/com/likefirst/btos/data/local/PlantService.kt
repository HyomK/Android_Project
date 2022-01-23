package com.likefirst.btos.data.local


import com.likefirst.btos.ui.profile.plant.PlantListView


class PlantService {


    private lateinit var plantView : PlantListView
    fun setPlantListView(plantView: PlantListView){
        this.plantView=plantView
    }

    fun getPlants(){

        //dummyDat

    }


}