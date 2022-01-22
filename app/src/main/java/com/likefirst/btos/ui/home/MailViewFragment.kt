package com.likefirst.btos.ui.home

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentMailViewBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.main.ReportFragment

class MailViewFragment:BaseFragment<FragmentMailViewBinding>(FragmentMailViewBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity
        val presFragment= this
        binding.mailViewBodyTv.text=arguments?.getString("body")

        val menuItem = resources.getStringArray(R.array.report_items)
        val adapter=ArrayAdapter( requireContext()!! ,R.layout.menu_dropdown_item, menuItem)
        binding.reportMenuList?.setAdapter(adapter)
        binding.reportMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))


        binding.letterViewSendBtn.setOnClickListener{
            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "답장을 보낼까요?",
                "btnData" to btn
            )
            // 버튼 클릭 이벤트 설정
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked(){
                }
                override fun onButton2Clicked() {

                    mActivity.ChangeFragment().hideFragment(R.id.main_layout,presFragment,
                        WriteMailFragment()
                    )

                }
            })
            dialog.show(mActivity.supportFragmentManager, "CustomDialog")

        }

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
                    dialog.setButtonClickListener(object:
                        CustomDialogFragment.OnButtonClickListener {
                        override fun onButton1Clicked() {}
                        override fun onButton2Clicked() {}
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    Log.d("selected", "moved")
                    mActivity.ChangeFragment().moveFragment(R.id.home_main_layout, ReportFragment())
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
                    dialog.setButtonClickListener(object:
                        CustomDialogFragment.OnButtonClickListener {
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
                            checkDialog.setButtonClickListener(object:
                                CustomDialogFragment.OnButtonClickListener {
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


        binding.letterViewToolbar.toolbarBackIc.setOnClickListener{
            val mActivity =activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
            onDestroyView()
        }



    }


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
                    dialog.setButtonClickListener(object:
                        CustomDialogFragment.OnButtonClickListener {
                        override fun onButton1Clicked() {}
                        override fun onButton2Clicked() {}
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    mActivity.ChangeFragment().moveFragment(R.id.letterView_main_layout,
                        ReportFragment()
                    )
                }
                //차단
                2 -> {
                    val btn= arrayOf("취소","차단")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "정말로 차단할까요?\n해당 발신인의 편지를 받지 않게 됩니다",
                        "btnData" to btn
                    )
                    // 버튼 클릭 이벤트 설정
                    dialog.setButtonClickListener(object:
                        CustomDialogFragment.OnButtonClickListener {
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
                            checkDialog.setButtonClickListener(object:
                                CustomDialogFragment.OnButtonClickListener {
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


