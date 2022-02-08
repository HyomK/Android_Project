package com.likefirst.btos.ui.profile.setting

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.likefirst.btos.ui.main.CustomDialogFragment

fun settingDialog(requireActivity : FragmentActivity, fragment : Fragment) {
    val dialog = CustomDialogFragment()
    val data = arrayOf("확인")
    dialog.arguments= bundleOf(
        "bodyContext" to "성공적으로 변경되었습니다.",
        "btnData" to data
    )
    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
        override fun onButton1Clicked() {
            requireActivity.supportFragmentManager.popBackStack()
        }
        override fun onButton2Clicked() {

        }
    })
    dialog.show(fragment.parentFragmentManager, "settingSuccess")

}