package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.databinding.FragmentAppinfoBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity


class AppInfoFragment: BaseFragment<FragmentAppinfoBinding>(FragmentAppinfoBinding::inflate), MainActivity.onBackPressedListener   {
    override fun initAfterBinding() {
        binding.appinfoToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.appinfoToolbar.toolbarTitleTv.text="앱 정보"
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}