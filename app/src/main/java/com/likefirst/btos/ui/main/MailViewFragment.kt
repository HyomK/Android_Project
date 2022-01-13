package com.likefirst.btos.ui.main

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.MenuRes
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentLetterViewBinding
import com.likefirst.btos.ui.BaseFragment
import java.util.*

class MailViewFragment:BaseFragment<FragmentLetterViewBinding>(FragmentLetterViewBinding::inflate) {

    override fun initAfterBinding() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.report_items, android.R.layout.simple_spinner_dropdown_item
        )
        binding.mailviewBodyTv.text=arguments?.getString("body")

        binding?.typesFilter?.setAdapter(adapter)
        binding?.typesFilter.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.typesFilter.onItemSelectedListener = SpinnerActivity()

        binding.letterviewSendBtn.setOnClickListener {
            it.setOnClickListener() {

                val wrapper :  Context = ContextThemeWrapper(requireContext(), R.style.PopupCustom)
                val popup: PopupMenu = PopupMenu(wrapper, it);
                //inflate menu with layout mainmenu
                popup.inflate(R.menu.report_menu)
                popup.show()
                popup.setOnMenuItemClickListener(PopmenuActiviy())

            }
        }

        binding.letterviewToolbar.toolbarBackIc.setOnClickListener{
            val mActivity =activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
            onDestroyView()
        }

    }

    inner class PopmenuActiviy (): Activity(), PopupMenu.OnMenuItemClickListener {


        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (item?.itemId == R.id.option_1){
                Log.d("success", "Success")
                return true
            }
            return false;
        }

    }


    fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext()!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        val mActivity = activity as MainActivity
        val dialog = CustomDialogFragment()
        var option = null

        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()

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

    inner class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            val mActivity = activity as MainActivity
            val dialog = CustomDialogFragment()
            when (pos) {
                //선택안함
                0 -> {

                }
                //삭제
                1 -> {
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
                2 -> {
                    mActivity.changeFragment(ReportFragment()).moveFragment(R.id.letterview_main_layout)
                }
                //차단
                3 -> {
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
                else -> {

                }

            }

        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }


}


