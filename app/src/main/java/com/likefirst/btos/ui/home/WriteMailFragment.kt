package com.likefirst.btos.ui.home

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentMailWriteBinding
import com.likefirst.btos.ui.BaseFragment
import android.text.Editable

import android.text.TextWatcher
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity


class WriteMailFragment:BaseFragment<FragmentMailWriteBinding>(FragmentMailWriteBinding::inflate) {

    override fun initAfterBinding() {

        Log.d("changedWrite", "change write")
        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter=ArrayAdapter( requireContext()!! ,R.layout.menu_dropdown_item, menuItem)
        binding.reportMenuList.onItemSelectedListener = SpinnerActivity()
        binding.reportMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.reportMenuList.setAdapter(adapter)

        binding.letterWriteToolbar.toolbarBackIc.setOnClickListener{
            val mActivity = activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
        }


        binding.letterWriteBodyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (null !=  binding.letterWriteBodyEt.layout && binding.letterWriteBodyEt.layout.lineCount > 50) {
                    binding.letterWriteBodyEt.text.delete( binding.letterWriteBodyEt.text.length - 1, binding.letterWriteBodyEt.text.length)
                }
            }
        })
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
                    dialog.setButtonClickListener(object:
                        CustomDialogFragment.OnButtonClickListener {
                        override fun onButton1Clicked() {
                        }

                        override fun onButton2Clicked() {
                        }
                    })
                    dialog.show(mActivity.supportFragmentManager, "CustomDialog")
                }

            }

        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }

    override fun onPause() {
        super.onPause()
//        val mActivity = activity as MainActivity
//        mActivity.notifyDrawerHandler("unlock")

    }

}