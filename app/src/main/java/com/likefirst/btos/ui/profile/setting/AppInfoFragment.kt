package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.databinding.FragmentAppinfoBinding
import com.likefirst.btos.ui.BaseFragment


class AppInfoFragment: BaseFragment<FragmentAppinfoBinding>(FragmentAppinfoBinding::inflate)  {
    override fun initAfterBinding() {
        binding.appinfoToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.appinfoToolbar.toolbarTitleTv.text="앱 정보"
    }
}