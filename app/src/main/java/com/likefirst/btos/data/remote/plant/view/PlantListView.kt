package com.likefirst.btos.data.remote.plant.view

import com.likefirst.btos.data.entities.Plant


interface PlantListView {
    fun onPlantListLoading()
    fun onPlantListSuccess(plantList: ArrayList<Plant>)
    fun onPlantListFailure(code: Int, message: String)

}
