package com.likefirst.btos.ui.main

import com.likefirst.btos.databinding.FragmentPremiumBinding
import com.likefirst.btos.ui.BaseFragment

class PremiumFragment : BaseFragment <FragmentPremiumBinding>(FragmentPremiumBinding :: inflate) {
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity
        binding.premiumToolbar.toolbarBackIc.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()
        }
    }

}