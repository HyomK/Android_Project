package com.likefirst.btos.ui.fragment.plant

import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
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