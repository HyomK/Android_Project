package com.likefirst.btos.ui.profile.plant

import android.content.Intent
import android.util.Log
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.databinding.FragmentPlantinfoBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import android.os.Bundle
import android.os.Parcelable


class PlantItemFragment:BaseFragment<FragmentPlantinfoBinding>(FragmentPlantinfoBinding::inflate) {


    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        val bundle = this.arguments
        if (bundle != null) {
           var plant = bundle.getParcelable<Plant>("plantItem")
            binding.plantinfoNameTv.text=plant?.plantName
            binding.plantinfoDetailTv.text=plant?.plantInfo
            binding.plantinfoSelectBtn.text=plant?.plantPrice.toString()
        }
        binding.plantinfoRtLayout.isClickable=true


        binding.plantinfoBackIc.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()
        }
    }


}