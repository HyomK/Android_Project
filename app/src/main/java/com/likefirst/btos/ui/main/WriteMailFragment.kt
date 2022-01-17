package com.likefirst.btos.ui.main

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






class WriteMailFragment:BaseFragment<FragmentMailWriteBinding>(FragmentMailWriteBinding::inflate) {

    override fun initAfterBinding() {

        Log.d("changedWrite", "change write")
        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter=ArrayAdapter( requireContext()!! ,R.layout.menu_dropdown_item, menuItem)
        binding.reportMenuList.onItemSelectedListener = SpinnerActivity()
        binding.reportMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.reportMenuList.setAdapter(adapter)

        binding.letterwriteToolbar.toolbarBackIc.setOnClickListener{
            val mActivity = activity as MainActivity
            mActivity.supportFragmentManager.popBackStack()
        }


        binding.letterwriteBodyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (null !=  binding.letterwriteBodyEt.layout && binding.letterwriteBodyEt.layout.lineCount > 50) {
                    binding.letterwriteBodyEt.text.delete( binding.letterwriteBodyEt.text.length - 1, binding.letterwriteBodyEt.text.length)
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
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
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

}