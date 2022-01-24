package com.likefirst.btos.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.likefirst.btos.data.entities.Plant

class Converters {
    @TypeConverter
    fun listToJson(value: List<Plant>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Plant>::class.java).toList()
}