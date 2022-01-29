package com.likefirst.btos.data.remote.service


import com.likefirst.btos.data.remote.view.PlantListView


class PlantService {

    private lateinit var plantView : PlantListView


    fun setPlantListView(plantView: PlantListView){
        this.plantView=plantView
    }

    fun getPlants(){

    }


}