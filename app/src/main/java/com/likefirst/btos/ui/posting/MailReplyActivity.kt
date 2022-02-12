package com.likefirst.btos.ui.posting

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMailReplyBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment

class MailReplyActivity: BaseActivity<ActivityMailReplyBinding>(ActivityMailReplyBinding::inflate){
    override fun initAfterBinding() {

        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter= ArrayAdapter(this, R.layout.menu_dropdown_item, menuItem)
        binding.MailReplyMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.MailReplyMenuList.setAdapter(adapter)
        binding.MailReplyHideView.visibility=View.VISIBLE
        binding.MailReplyMenuSp.visibility=View.GONE

        binding.MailReplyToolbar.toolbarBackIc.setOnClickListener {
            onBackPressed()
        }
        binding.MailReplyCheckBtn.setOnClickListener {
            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "답장을 보낼까요?",
                "btnData" to btn
            )
            // 버튼 클릭 이벤트 설정
            dialog.setButtonClickListener(object:
                CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked() {
                }
                override fun onButton2Clicked() {
                    binding.MailReplyMenuBtn.visibility= View.VISIBLE
                    binding.MailReplyWriteBtn.visibility=View.VISIBLE
                    binding.MailReplyHideView.visibility=View.VISIBLE
                    binding.MailReplyCheckBtn.visibility=View.GONE
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")
            binding.MailReplyMenuSp.visibility=View.VISIBLE
        }

        binding.MailReplyWriteBtn.setOnClickListener {
            binding.MailReplyMenuBtn.visibility= View.INVISIBLE
            binding.MailReplyWriteBtn.visibility=View.INVISIBLE
            binding.MailReplyCheckBtn.visibility=View.VISIBLE
         //   binding.MailReplyHideView.visibility=View.VISIBLE
        }

        binding.MailReplyMenuList.setOnItemClickListener { adapterView, view, i, l ->
            val dialog = CustomDialogFragment()
            binding.MailReplyHideView.visibility=View.VISIBLE
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
                        override fun onButton1Clicked() {
                        }
                        override fun onButton2Clicked() {
                        }
                    })
                    dialog.show(supportFragmentManager, "CustomDialog")
                }
            }

        }

        binding.MailReplyBodyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                Log.d("MailSelection", binding.MailReplyBodyEt.selectionStart.toString())
                Log.d("MailLength", binding.MailReplyBodyEt.text.length.toString())
                if (null !=  binding.MailReplyBodyEt.layout && binding.MailReplyBodyEt.layout.lineCount > 50) {
                    binding.MailReplyBodyEt.text.delete( binding.MailReplyBodyEt.selectionStart - 1, binding.MailReplyBodyEt.selectionStart)
                }
            }
        })


    }
}
