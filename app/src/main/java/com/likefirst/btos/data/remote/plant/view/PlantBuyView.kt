package com.likefirst.btos.data.remote.plant.view

import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.presentation.View.main.CustomDialogFragment


interface PlantBuyView{
    fun onPlantBuyError(Dialog:CustomDialogFragment)
    fun onPlantBuySuccess(plantIdx:Int, request: PlantResponse)
    fun onPlantBuyFailure(code: Int, message: String)
}