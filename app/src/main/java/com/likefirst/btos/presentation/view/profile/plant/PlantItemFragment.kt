package com.likefirst.btos.presentation.view.profile.plant

import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.databinding.FragmentPlantinfoBinding
import com.likefirst.btos.presentation.BaseFragment
import com.likefirst.btos.presentation.view.main.MainActivity
import android.view.View.GONE
import com.likefirst.btos.R
import java.text.NumberFormat
import java.util.*


class PlantItemFragment:BaseFragment<FragmentPlantinfoBinding>(FragmentPlantinfoBinding::inflate) {


    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        val bundle = this.arguments
        if (bundle != null) {
           var plant = bundle.getParcelable<Plant>("plantItem")!!
            binding.plantinfoNameTv.text=plant.plantName
            binding.plantinfoDetailTv.text=plant.plantInfo
            binding.plantinfoIv.setImageResource( requireContext()!!.resources.getIdentifier(
                plantName[plant.plantIdx-1]
                        +"_"+plant.maxLevel.toString()
                        +"_full","drawable",
                requireActivity().packageName))
            if(!plant.isOwn){
                val won = NumberFormat.getCurrencyInstance(Locale.KOREA).format(plant.plantPrice)
                binding.plantinfoSelectBtn.text=won
            }else{
                binding.plantinfoSelectBtn.visibility=GONE;
            }


        }
        binding.plantinfoRtLayout.isClickable=true


        binding.plantinfoBackIc.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

}