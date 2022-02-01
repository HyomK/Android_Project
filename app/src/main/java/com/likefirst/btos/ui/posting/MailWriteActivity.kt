package com.likefirst.btos.ui.posting

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMailWriteBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity





class MailWriteActivity:BaseActivity<ActivityMailWriteBinding>(ActivityMailWriteBinding::inflate) {


    override fun initAfterBinding() {

        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter= ArrayAdapter(this, R.layout.menu_dropdown_item, menuItem)
        binding.MailWriteMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.MailWriteMenuList.setAdapter(adapter)
        binding.MailWriteHideView.visibility=View.VISIBLE

        binding.MailWriteToolbar.toolbarBackIc.setOnClickListener {
            onBackPressed()
        }

        binding.MailWriteBodyEt.hint="|누군가 들어줬으면 하는 말이 있나요?"

        binding.MailWriteCheckBtn.setOnClickListener {
            val dialog = CustomDialogFragment()
            val btn= arrayOf("아니오","예")
            dialog.arguments= bundleOf(
                "bodyContext" to
                        "편지를 작성할까요?" +
                        "\n편지를 보내면 랜덤한 사람에게 지금 바로 보내지고,답장을 받을 수 있습니다." +
                        "\n(광고 재생 후 작성페이지로 전환됩니다.)",
                "btnData" to btn
            )
            // 버튼 클릭 이벤트 설정
            dialog.setButtonClickListener(object:
                CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked() {
                }
                override fun onButton2Clicked() {
                    binding.MailWriteMenuBtn.visibility= View.VISIBLE
                    binding.MailWriteWriteBtn.visibility=View.VISIBLE
                    binding.MailWriteHideView.visibility=View.VISIBLE
                    binding.MailWriteCheckBtn.visibility=View.GONE
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")
        }

        binding.MailWriteMenuList.setOnItemClickListener { adapterView, view, i, l ->
            val dialog = CustomDialogFragment()
            binding.MailWriteHideView.visibility=View.VISIBLE
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


        binding.MailWriteWriteBtn.setOnClickListener {
            binding.MailWriteMenuBtn.visibility= View.INVISIBLE
            binding.MailWriteWriteBtn.visibility=View.INVISIBLE
            binding.MailWriteCheckBtn.visibility=View.VISIBLE
            //   binding.MailReplyHideView.visibility=View.VISIBLE
        }


        binding.MailWriteBodyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                Log.d("MailSelection", binding.MailWriteBodyEt.selectionStart.toString())
                Log.d("MailLength", binding.MailWriteBodyEt.text.length.toString())
                if (null !=  binding.MailWriteBodyEt.layout && binding.MailWriteBodyEt.layout.lineCount > 50) {
                    binding.MailWriteBodyEt.text.delete( binding.MailWriteBodyEt.selectionStart - 1, binding.MailWriteBodyEt.selectionStart)
                }
            }
        })
    }


}