package com.likefirst.btos.data.remote.view.plant

import com.likefirst.btos.data.remote.response.PlantRequest
import com.likefirst.btos.data.remote.response.PlantResponse
import com.likefirst.btos.ui.main.CustomDialogFragment


interface PlantBuyView{
    fun onPlantBuyError(Dialog:CustomDialogFragment)
    fun onPlantBuySuccess(plantIdx:Int, request: PlantResponse)
    fun onPlantBuyFailure(code: Int, message: String)
}