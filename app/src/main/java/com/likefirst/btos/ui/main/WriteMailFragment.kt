package com.likefirst.btos.ui.main

import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentLetterViewBinding
import com.likefirst.btos.ui.BaseFragment

class WriteMailFragment:BaseFragment<FragmentLetterViewBinding>(FragmentLetterViewBinding::inflate) {
    override fun initAfterBinding() {
        binding.mailviewBodyTv.text=arguments?.getString("body")

        binding.letterviewToolbar.toolbarBackIc.setOnClickListener{
            val mActivity = activity as MainActivity
            mActivity.supportFragmentManager.popBackStack("mailbox", POP_BACK_STACK_INCLUSIVE)

        }

    }
}