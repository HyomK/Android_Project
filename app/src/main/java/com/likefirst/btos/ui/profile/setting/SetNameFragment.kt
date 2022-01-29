package com.likefirst.btos.ui.profile.setting

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
        binding.nicknameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (null !=  binding.nicknameEdit.layout && binding.nicknameEdit.layout.lineCount > 1) {
                    binding.nicknameEdit.text.delete( binding.nicknameEdit.selectionStart - 1, binding.nicknameEdit.selectionStart)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.nicknameEdit.text.length >10)  {
                    binding.nameError.visibility= View.VISIBLE
                    binding.nameError.text="10자 이내로 작성해주세요"
                }
                else {
                    binding.nameError.visibility= View.INVISIBLE
                }
            }
        })

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