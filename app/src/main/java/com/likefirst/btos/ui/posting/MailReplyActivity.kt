package com.likefirst.btos.ui.posting

import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.posting.response.*
import com.likefirst.btos.data.remote.posting.service.SendService
import com.likefirst.btos.data.remote.posting.view.SendReplyView
import com.likefirst.btos.databinding.ActivityMailReplyBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.getUserIdx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class MailReplyActivity: BaseActivity<ActivityMailReplyBinding>(ActivityMailReplyBinding::inflate),SendReplyView{


    lateinit var reply : MailInfoResponse
    private var isSuccess= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reply= intent.getParcelableExtra<MailInfoResponse>("reply")!!
    }


    override fun initAfterBinding() {
        val menuItem = resources.getStringArray(R.array.delete_items)
        val adapter= ArrayAdapter(this, R.layout.menu_dropdown_item, menuItem)
        binding.MailReplyMenuList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.MailReplyMenuList.setAdapter(adapter)
        binding.MailReplyHideView.visibility=View.VISIBLE
        binding.MailReplyMenuSp.visibility=View.GONE
        binding.mailReplyDateTv.text = dateToString(Date())
        initListener()

    }

    fun initListener(){
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
                    val replyService = SendService()
                    replyService.setSendReplyView(this@MailReplyActivity)

                    val request = SendReplyRequest(getUserIdx(),reply.senderIdx,reply.firstHistoryType,reply.typeIdx,binding.MailReplyBodyEt.text.toString())
                    replyService.sendReply(request)
                    Log.e("Reply-api-done","$isSuccess")
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")

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
                if (null !=  binding.MailReplyBodyEt.layout && binding.MailReplyBodyEt.layout.lineCount > 50) {
                    binding.MailReplyBodyEt.text.delete( binding.MailReplyBodyEt.selectionStart - 1, binding.MailReplyBodyEt.selectionStart)
                }
            }
        })

    }

    override fun onBackPressed() {
        Log.e("Reply-api","$isSuccess")
        if(isSuccess) finish()
        else{
            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "작성을 취소할까요?\n작성중이던 내용이 사라집니다",
                "btnData" to btn
            )
            dialog.setButtonClickListener(object:
                CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked() {  }
                override fun onButton2Clicked() { finish() } })
            dialog.show(supportFragmentManager,"")
        }
    }


    fun setLoadingView(){
        binding.mailReplyLoadingPb.visibility= View.VISIBLE
        binding.mailReplyLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }
    override fun onSendReplyLoading() {
        setLoadingView()
    }

    override fun onSendReplySuccess(result: String) {
        Log.e("Reply-api",result.toString())
        isSuccess=true
        binding.mailReplyLoadingPb.visibility=View.GONE
        binding.MailReplyMenuSp.visibility=View.VISIBLE
        binding.MailReplyMenuBtn.visibility= View.VISIBLE
        binding.MailReplyHideView.visibility=View.VISIBLE
        binding.MailReplyCheckBtn.visibility=View.GONE
        binding.MailReplyBodyEt.isFocusable=false
        binding.MailReplyBodyEt.isClickable=false
        binding.MailReplyBodyEt.isFocusableInTouchMode=false // 입력막기
        Toast.makeText(this,"편지가 발송되었습니다.",Toast.LENGTH_SHORT)

    }
    override fun onSendReplyFailure(code: Int, message: String) {
        Log.e("Reply-api-fail",message.toString())
    }
}