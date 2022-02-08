package com.likefirst.btos.ui.profile.setting

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.likefirst.btos.data.entities.UserName
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.FragmentNicknameBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity

class SetNameFragment:BaseFragment<FragmentNicknameBinding>(FragmentNicknameBinding::inflate),
    MainActivity.onBackPressedListener, SetSettingUserView {

    //TODO : 한글키보드만 가능하도록, 엔터키가 있는 키보드로 변경
    override fun initAfterBinding() {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        val settingService = SettingUserService()

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
                //TODO : 한글만 가능, 비속어 처리

            }
        })

        binding.nicknameDoneBtn.setOnClickListener {
//            if(binding.nicknameEdit.text.length <= 10){
//                //유효성 검사 api
//                var isSuccess =true
//
//                if(isSuccess){
                    settingService.setSettingUserView(this)
                    settingService.setName(userDB!!.getUserIdx(), UserName(binding.nicknameEdit.text.toString()))
//                }else{
//
//                }
//            }else{
//                binding.nameError.text="10자 이내로 작성해주세요"
//                binding.nameError.visibility= View.VISIBLE
//            }
        }
    }
    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSetSettingUserViewLoading() {
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateNickName(binding.nicknameEdit.text.toString())
        Log.e("SETNAME",userDB.getUser().toString())
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "닉네임이 성공적으로 변경되었습니다.",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {
                requireActivity().supportFragmentManager.popBackStack()
            }
            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.parentFragmentManager, "setNameSuccess")
    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.nameError.text=message
        binding.nameError.visibility= View.VISIBLE
    }

}