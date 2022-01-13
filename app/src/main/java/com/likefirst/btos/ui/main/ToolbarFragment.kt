package com.likefirst.btos.ui.main

import com.likefirst.btos.databinding.ActivityMainBinding.inflate
import com.likefirst.btos.databinding.ToolbarLayoutBinding
import com.likefirst.btos.ui.BaseFragment

class ToolbarFragment:BaseFragment<ToolbarLayoutBinding>(ToolbarLayoutBinding::inflate) {

    override fun initAfterBinding() {
       binding.toolbarBackIc.setOnClickListener {

       }
    }


}