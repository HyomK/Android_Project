package com.likefirst.btos.ui.profile.plant

import com.likefirst.btos.data.entities.Plant

interface PlantListView {
    fun onPlantListLoading()
    fun onPlantListSuccess(plants : ArrayList<Plant>)
    fun onPlantListFailure(code: Int, message: String)
}