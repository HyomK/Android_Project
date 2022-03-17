package com.likefirst.btos.utils

import androidx.core.os.bundleOf
import com.likefirst.btos.presentation.view.main.CustomDialogFragment


fun errorDialog(): CustomDialogFragment {
    val errorDialog = CustomDialogFragment()
    val btn= arrayOf("확인")
    errorDialog.arguments= bundleOf(
        "bodyContext" to "인터넷 접속이 불안정합니다.\n인터넷 접속 상태를 확인해 주세요",
        "btnData" to btn
    )
    errorDialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
        override fun onButton1Clicked() {
        }
        override fun onButton2Clicked() {
        }
    })
    return errorDialog
}
