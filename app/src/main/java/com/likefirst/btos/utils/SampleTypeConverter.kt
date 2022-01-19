package com.likefirst.btos.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SampleTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): List<Plant?>? {
            val listType: Type = object : TypeToken<List<Plant?>?>() {}.type
            return Gson().fromJson(value, listType)
        }
        @TypeConverter
        @JvmStatic
        fun listToString(list: List<Plant?>?): String? {
            val gson = Gson()
            return gson.toJson(list)
        }
    }


}