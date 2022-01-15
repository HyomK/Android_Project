package com.likefirst.btos.ui.main

import android.app.Activity
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.likefirst.btos.R
import com.likefirst.btos.databinding.DialogMailPopupBinding.inflate
import com.likefirst.btos.databinding.FragmentMailWriteBinding
import com.likefirst.btos.ui.BaseFragment

class WriteMailFragment:BaseFragment<FragmentMailWriteBinding>(FragmentMailWriteBinding::inflate) {
    override fun initAfterBinding() {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.report_items, android.R.layout.simple_spinner_dropdown_item
        )

        binding.reportMenuList?.setAdapter(adapter)
        binding.reportMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.reportMenuList.onItemSelectedListener = SpinnerActivity()
        binding.reportMenuList.gravity = Gravity.CENTER

        binding.letterwriteToolbar.toolbarBackIc.setOnClickListener{
            val mActivity = activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
        }



    }

    inner class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            val mActivity = activity as MainActivity
            val dialog = CustomDialogFragment()
            when (pos) {
                //선택안함

                //삭제
                0 -> {
                    val btn= arrayOf("확인","취소")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "정말 삭제하시겠습니까?",
                        "btnData" to btn
                    )
                    // 버튼 클릭 이벤트 설정
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {
                        }

                        override fun onButton2Clicked() {
                        }
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    mActivity.changeFragment().moveFragment(R.id.letterview_main_layout,ReportFragment())
                }
                //차단
                2 -> {
                    val btn= arrayOf("취소","차단")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "정말로 차단할까요?\n해당 발신인의 편지를 받지 않게 됩니다",
                        "btnData" to btn
                    )
                    // 버튼 클릭 이벤트 설정
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {
                        }
                        override fun onButton2Clicked() {
                            val checkDialog = CustomDialogFragment()
                            val Checkbtn= arrayOf("확인")
                            checkDialog .arguments= bundleOf(
                                "bodyContext" to "이제 해당인으로부터의 편지를 수신하지 않습니다.\n설정탭에서 차단해제가 가능합니다",
                                "btnData" to Checkbtn
                            )
                            // 버튼 클릭 이벤트 설정
                            checkDialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                                override fun onButton1Clicked() {
                                }
                                override fun onButton2Clicked() {
                                }
                            })
                            checkDialog.show(mActivity.supportFragmentManager, "CustomDialog")
                        }
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")

                }
                //일치하는게 없는 경우

            }

        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }

}