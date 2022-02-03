package com.likefirst.btos.data.remote.view.plant

import com.likefirst.btos.data.remote.response.PlantResponse

interface PlantInitView {
    fun onPlantInitSuccess(userId: Int)
    fun onPlantInitFailure(code: Int, message: String)
}