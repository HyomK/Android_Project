package com.likefirst.btos.data.remote.plant.view

import com.likefirst.btos.data.remote.plant.response.PlantResponse

interface PlantInitView {
    fun onPlantInitSuccess(userId: Int)
    fun onPlantInitFailure(code: Int, message: String)
}