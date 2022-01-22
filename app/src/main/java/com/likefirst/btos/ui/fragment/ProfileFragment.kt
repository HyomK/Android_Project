package com.likefirst.btos.ui.fragment

import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentProfileBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.fragment.plant.PlantFragment
import com.likefirst.btos.ui.fragment.premium.PremiumFragment
import com.likefirst.btos.ui.main.MainActivity

class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity
       binding.profilePremiumTv.setOnClickListener {
           mActivity.ChangeFragment().hideFragment(R.id.fr_layout,this, PlantFragment())
       }

        binding.profileCd.setOnClickListener {
            mActivity.ChangeFragment().hideFragment(R.id.fr_layout,this, PlantFragment())
        }

        binding.profilePremiumTv.setOnClickListener {
            mActivity.ChangeFragment().hideFragment(R.id.fr_layout,this, PremiumFragment())
        }
    }
}