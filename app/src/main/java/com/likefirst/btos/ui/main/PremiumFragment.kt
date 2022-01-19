package com.likefirst.btos.ui.main

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.likefirst.btos.ApplicationClass.Companion.TAG
import com.likefirst.btos.databinding.FragmentPremiumBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.utils.Plant
import com.likefirst.btos.utils.PlantDatabase
import org.json.JSONObject

class PremiumFragment :BaseFragment<FragmentPremiumBinding>(FragmentPremiumBinding :: inflate) {
    override fun initAfterBinding() {
//
//        val jsonString = getJsonDataFromAsset()
//        val gson= Gson()
//        val plantObjType = object: TypeToken<List<Plant>>() {}.type
//        val plantObj : List<Plant> = gson.fromJson(jsonString, plantObjType)


        loadData()



    }

    fun loadData(){
        val assetManager = resources.assets
        val inputStream= assetManager.open("PlantDummy.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val gson=Gson()
        val plantObjType = object: TypeToken<Plant>() {}.type
        val plantDB = PlantDatabase.getInstance(requireContext()!!)
        val jObject = JSONObject(jsonString)
        val jArray = jObject.getJSONArray("result")



        for (i in 0 until jArray.length()) {
            val obj = jArray.getJSONObject(i)
            val plantObj = gson.fromJson(obj.toString() , Plant::class.java)
            plantDB?.plantDao()?.insert(plantObj)
            val title = plantObj.currentLevel
            Log.d(TAG, "title($i): $title ")
         }
        var plantList= plantDB?.plantDao()?.getPlants()

        Log.d(TAG, "size($plantList.size):" )


    }




}