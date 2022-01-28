package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.databinding.FragmentFontBinding
import com.likefirst.btos.databinding.FragmentSettingBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class SetFontFragment:BaseFragment<FragmentFontBinding>(FragmentFontBinding::inflate), MainActivity.onBackPressedListener  {
    override fun initAfterBinding() {

        binding.fontToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.fontToolbar.toolbarTitleTv.text="폰트"
    }



    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

}