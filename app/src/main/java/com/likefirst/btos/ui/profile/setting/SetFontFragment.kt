package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.databinding.FragmentFontBinding
import com.likefirst.btos.databinding.FragmentSettingBinding
import com.likefirst.btos.ui.BaseFragment

class SetFontFragment:BaseFragment<FragmentFontBinding>(FragmentFontBinding::inflate) {
    override fun initAfterBinding() {

        binding.fontToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.fontToolbar.toolbarTitleTv.text="폰트"
    }
}