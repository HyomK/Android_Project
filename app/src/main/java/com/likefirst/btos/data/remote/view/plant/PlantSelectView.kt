package com.likefirst.btos.data.remote.view.plant

import com.likefirst.btos.data.remote.response.PlantResponse
import com.likefirst.btos.ui.main.CustomDialogFragment

interface PlantSelectView{
    fun onPlantSelectError(Dialog : CustomDialogFragment)
    fun onPlantSelectSuccess(plantIdx:Int ,request: PlantResponse)
    fun onPlantSelectFailure(code: Int, message: String)
}