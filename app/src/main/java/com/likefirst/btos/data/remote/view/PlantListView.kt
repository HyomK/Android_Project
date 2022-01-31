package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.entities.Plant

interface PlantListView {
    fun onPlantListLoading()
    fun onPlantListSuccess(plantList: ArrayList<Plant>)
    fun onPlantListFailure(code: Int, message: String)

    fun onPlantSelectSuccess(isSucess:Boolean)
    fun onPlantSelectFailure(code: Int, message: String)

    fun onPlantBuySuccess(isSucess:Boolean)
    fun onPlantBuyFailure(code: Int, message: String)
}