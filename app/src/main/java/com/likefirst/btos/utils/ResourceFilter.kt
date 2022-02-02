package com.likefirst.btos.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.ui.main.MainActivity

class resourceFilter:Fragment(){

    private lateinit var plantName:Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return null
    }
    fun getPlantProfile(plantIdx:Int, level:Int):Int{
        plantName=requireContext().resources.getStringArray(R.array.plantEng)
        return requireContext().resources.getIdentifier(plantName[plantIdx-1]+"_"+level.toString()+"_circle","drawable",requireActivity().packageName)
    }
}