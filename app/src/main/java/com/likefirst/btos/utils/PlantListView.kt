package com.likefirst.btos.utils

interface PlantListView {
    fun onPlantListLoading()
    fun onPlantListSuccess(plants : ArrayList<Plant>)
    fun onPlantListFailure(code: Int, message: String)
}