package com.likefirst.btos.data.remote.plant.view

import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.ui.main.CustomDialogFragment

interface PlantSelectView{
    fun onPlantSelectError(Dialog : CustomDialogFragment)
    fun onPlantSelectSuccess(plantIdx:Int ,request: PlantResponse)
    fun onPlantSelectFailure(code: Int, message: String)
}