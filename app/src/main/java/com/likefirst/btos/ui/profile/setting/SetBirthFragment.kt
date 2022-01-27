package com.likefirst.btos.ui.profile.setting

import android.util.Log
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentBirthBinding
import com.likefirst.btos.ui.BaseFragment

class SetBirthFragment:BaseFragment<FragmentBirthBinding>(FragmentBirthBinding::inflate) {
    override fun initAfterBinding() {

        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.onboarding_dropdown_item,agelist)
        binding.birthList.setAdapter(arrayAdapter)
        binding.birthList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.birthList.dropDownHeight=450

        var item=0
//        binding.birthList.setOnItemClickListener { adapterView, view, i, l ->
//            run {
//                item = adapterView.getPositionForView(view)
//                Log.d("Birth", agelist[item])
//
//            }
//        }


        binding.birthToolbar.toolbarTitleTv.text="생년 변경"
        binding.birthToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


    }
}