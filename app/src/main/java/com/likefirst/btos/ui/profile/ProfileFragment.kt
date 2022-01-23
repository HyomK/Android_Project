package com.likefirst.btos.ui.profile

import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentProfileBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.home.MailboxFragment
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.premium.PremiumFragment
import com.likefirst.btos.ui.main.MainActivity

class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity

        binding.profileCd.setOnClickListener {

            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fr_layout,  PlantFragment(), "profile")
                .addToBackStack(null)
                .commit()
            //mActivity.ChangeFragment().hideFragment(R.id.fr_layout,this, PlantFragment())
        }

        binding.profilePremiumTv.setOnClickListener {

            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, PremiumFragment())
                .hide(this)
                .show(PremiumFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}