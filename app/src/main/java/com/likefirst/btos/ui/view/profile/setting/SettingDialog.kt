package com.likefirst.btos.ui.view.profile.setting

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.likefirst.btos.ui.view.main.CustomDialogFragment


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

fun settingDialogRestart(requireActivity : FragmentActivity, fragment : Fragment, context : Context){
    val dialog = CustomDialogFragment()
    val data = arrayOf("확인")
    dialog.arguments= bundleOf(
        "bodyContext" to "성공적으로 변경되었습니다. 어플을 재시작 합니다.",
        "btnData" to data
    )
    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
        override fun onButton1Clicked() {
            requireActivity.supportFragmentManager.popBackStack()
            val packageManager = context.packageManager
            val mIntent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = mIntent!!.component
            val restartIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(restartIntent)
            Runtime.getRuntime().exit(0)
        }
        override fun onButton2Clicked() {

        }
    })
    dialog.show(fragment.parentFragmentManager, "settingSuccess, restart application")
}