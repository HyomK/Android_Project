package com.likefirst.btos.ui.profile.setting

import android.view.View
import com.likefirst.btos.databinding.FragmentNicknameBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class SetNameFragment:BaseFragment<FragmentNicknameBinding>(FragmentNicknameBinding::inflate),
    MainActivity.onBackPressedListener  {
    override fun initAfterBinding() {

        binding.nicknameToolbar.toolbarTitleTv.text="닉네임 변경"
        binding.nicknameToolbar.toolbarBackIc.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.nicknameEdit.setOnFocusChangeListener { view, b ->  }

        binding.nicknameDoneBtn.setOnClickListener {
            if(binding.nicknameEdit.text.length <= 10){
                //유효성 검사 api
                var isSuccess =true

                if(isSuccess){

                }else{
                    binding.nameError.text="중복된 닉네임 입니다"
                    binding.nameError.visibility= View.VISIBLE
                }
            }else{
                binding.nameError.text="10자 이내로 작성해주세요"
                binding.nameError.visibility= View.VISIBLE
            }
        }
    }
    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

}