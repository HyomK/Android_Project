package com.likefirst.btos.ui.profile.plant

import android.util.Log
import com.likefirst.btos.databinding.FragmentPlantinfoBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class PlantItemFragment:BaseFragment<FragmentPlantinfoBinding>(FragmentPlantinfoBinding::inflate) {


    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        binding.plantinfoRtLayout.isClickable=true
        binding.plantinfoBackIc.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()
        }
    }


}