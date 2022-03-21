package com.likefirst.btos.domain.repository

import com.likefirst.btos.data.entities.Plant

interface PlantNotificationRepository {
    fun getSelectedPlant(): Plant
    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean)
}