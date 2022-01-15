package com.likefirst.btos.ui.main

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.MenuRes
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentMailViewBinding
import com.likefirst.btos.ui.BaseFragment
import java.util.*

class MailViewFragment:BaseFragment<FragmentMailViewBinding>(FragmentMailViewBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        binding.mailviewBodyTv.text=arguments?.getString("body")

        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.report_items, android.R.layout.simple_spinner_dropdown_item
        )

        binding.reportMenuList?.setAdapter(adapter)
        binding.reportMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.reportMenuList.setOnItemClickListener { adapterView, view, i, l ->
            val dialog = CustomDialogFragment()
            when (i) {
                //삭제
                0 -> {
                    val btn= arrayOf("확인","취소")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "정말 삭제하시겠습니까?",
                        "btnData" to btn
                    )
                    // 버튼 클릭 이벤트 설정
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {}
                        override fun onButton2Clicked() {}
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    Log.d("selected", "moved")
                    mActivity.ChangeFragment().moveFragment(R.id.home_main_layout,ReportFragment())
                   // mActivity.supportFragmentManager.popBackStack()
                    onDestroyView()
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


            }

        }



        binding.letterviewSendBtn.setOnClickListener{
            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "답장을 보낼까요?",
                "btnData" to btn
            )
            // 버튼 클릭 이벤트 설정
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                override fun onButton1Clicked(){
                }
                override fun onButton2Clicked() {
                    mActivity.ChangeFragment().moveFragment(R.id.letterview_main_layout,WriteMailFragment())
                }
            })
            dialog.show(mActivity.supportFragmentManager, "CustomDialog")

        }

        binding.letterviewToolbar.toolbarBackIc.setOnClickListener{
            val mActivity =activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
            onDestroyView()
        }



    }

//    inner class PopmenuActiviy (): Activity(), PopupMenu.OnMenuItemClickListener {
//
//
//        override fun onMenuItemClick(item: MenuItem?): Boolean {
//            if (item?.itemId == R.id.option_1){
//                Log.d("success", "Success")
//                return true
//            }
//            return false;
//        }
//
//    }


    inner class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val mActivity = activity as MainActivity
            val dialog = CustomDialogFragment()
            Log.d("selected", "selected")
            when (p2) {
                //삭제
                0 -> {
                    val btn= arrayOf("확인","취소")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "정말 삭제하시겠습니까?",
                        "btnData" to btn
                    )
                    // 버튼 클릭 이벤트 설정
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {}
                        override fun onButton2Clicked() {}
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    mActivity.ChangeFragment().moveFragment(R.id.letterview_main_layout,ReportFragment())
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


            }

        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            Log.d("selected", "nothing")
        }

//        override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//            Log.d("selected", "clicked")
//        }
    }

//    fun showMenu(v: View, @MenuRes menuRes: Int) {
//        val popup = PopupMenu(requireContext()!!, v)
//        popup.menuInflater.inflate(menuRes, popup.menu)
//        val mActivity = activity as MainActivity
//        val dialog = CustomDialogFragment()
//        var option = null
//
//        popup.setOnDismissListener {
//            // Respond to popup being dismissed.
//        }
//        // Show the popup menu.
//        popup.show()
//
//        val btn= arrayOf("확인","취소")
//        dialog.arguments= bundleOf(
//            "bodyContext" to "정말 삭제하시겠습니까?",
//            "btnData" to btn
//        )
//        // 버튼 클릭 이벤트 설정
//        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
//            override fun onButton1Clicked() {
//            }
//
//            override fun onButton2Clicked() {
//            }
//        })
//        dialog.show(mActivity.supportFragmentManager, "CustomDialog")
//    }





}

