package com.likefirst.btos.ui.view.posting

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.posting.response.SendLetterRequest
import com.likefirst.btos.data.remote.posting.service.SendService
import com.likefirst.btos.data.remote.posting.view.SendLetterView
import com.likefirst.btos.databinding.ActivityMailWriteBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.view.main.CustomDialogFragment
import com.likefirst.btos.utils.getUserIdx


class MailWriteActivity:BaseActivity<ActivityMailWriteBinding>(ActivityMailWriteBinding::inflate),SendLetterView {


    override fun initAfterBinding() {
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter= ArrayAdapter(this, R.layout.menu_dropdown_item, menuItem)

        setFont(userDB.getFontIdx()!!)
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
                    binding.MailWriteCheckBtn.visibility=View.GONE
                    val sendService = SendService()
                    sendService.setSendLetterView(this@MailWriteActivity)
                    sendService.sendLetter(SendLetterRequest(getUserIdx(),binding.MailWriteBodyEt.text.toString()))
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")
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

    override fun onSendLetterLoading() {
        setLoadingView()
    }

    override fun onSendLetterSuccess() {
        binding.mailWriteLoadingPb.visibility=View.GONE
        binding.MailWriteBodyEt.isFocusableInTouchMode=false
        binding.MailWriteBodyEt.isFocusable=false
        binding.MailWriteBodyEt.isClickable=false
        binding.MailWriteBodyEt.isCursorVisible=false
        Snackbar.make(binding.root,"편지가 발송되었습니다!",Snackbar.LENGTH_SHORT).show()
        Log.e("SENDING", "success")
    }

    override fun onSendLetterFailure(code: Int, message: String) {
        Log.e("SENDING", "${code} ${message}")
    }

    fun setFont(idx:Int){
        val fonts= resources.getStringArray(R.array.fontEng)
        val fontId= resources.getIdentifier(fonts[idx],"font", packageName)
        binding.MailWriteBodyEt.typeface=ResourcesCompat.getFont(this,fontId)

    }
    fun setLoadingView(){
        binding.mailWriteLoadingPb.visibility= View.VISIBLE
        binding.mailWriteLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }
}