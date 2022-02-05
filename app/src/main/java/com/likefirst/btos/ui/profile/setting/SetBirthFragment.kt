package com.likefirst.btos.ui.profile.setting

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserBirth
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.FragmentBirthBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity

class SetBirthFragment:BaseFragment<FragmentBirthBinding>(FragmentBirthBinding::inflate)
    , MainActivity.onBackPressedListener, SetSettingUserView  {
    override fun initAfterBinding() {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        val settingService = SettingUserService()

        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.onboarding_dropdown_item,agelist)
        binding.birthList.setAdapter(arrayAdapter)
        binding.birthList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.birthList.dropDownHeight=600

        var item=0

        binding.birthToolbar.toolbarTitleTv.text="생년 변경"
        binding.birthToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.birthDoneBtn.setOnClickListener {
            settingService.setSettingUserView(this)
            settingService.setBirth(userDB!!.getUserIdx(), UserBirth(binding.birthList.text.toString().toInt()))
        }
    }


    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSetSettingUserViewLoading() {
        binding.birthLoadingPb.visibility = View.VISIBLE
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        binding.birthLoadingPb.visibility = View.GONE
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateBirth(binding.birthList.text.toString().toInt())
        Log.e("SETBIRTH",userDB.getUser().toString())
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "생일이 성공적으로 변경되었습니다.",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {
                requireActivity().supportFragmentManager.popBackStack()
            }
            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.parentFragmentManager, "setBirthSuccess")
    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.birthLoadingPb.visibility = View.GONE
        binding.birthError.text=message
        binding.birthError.visibility= View.VISIBLE
    }

}