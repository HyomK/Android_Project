package com.likefirst.btos.presentation.View.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.users.response.BlackList
import com.likefirst.btos.data.remote.users.service.BlackListService
import com.likefirst.btos.data.remote.users.view.SetBlockView
import com.likefirst.btos.databinding.ActivityMailViewBinding

import com.likefirst.btos.presentation.BaseActivity
import com.likefirst.btos.presentation.View.main.CustomDialogFragment
import com.likefirst.btos.presentation.View.posting.MailReplyActivity
import dagger.hilt.android.AndroidEntryPoint


class MailViewActivity : BaseActivity<ActivityMailViewBinding>(ActivityMailViewBinding::inflate),SetBlockView{

    override fun initAfterBinding() {
        val bundle : Bundle = intent.getBundleExtra("MailView")!!
        val mail : MailInfoResponse? =bundle.getParcelable("mail")
        val blockService = BlackListService()
        blockService.setBlockView(this)
        binding.mailViewBodyTv.text= mail?.content


        if(mail?.senderNickName =="저편너머"){
            binding.mailViewDateTv.visibility= View.GONE
            binding.mailViewSenderTv.visibility=View.GONE
        }else{
            binding.mailViewDateTv.text=mail?.sendAt
            binding.mailViewSenderTv.text=mail?.senderNickName
        }
        setFont(mail?.senderFontIdx!!)

        val menuItem = resources.getStringArray(R.array.report_items)
        val adapter= ArrayAdapter( this ,R.layout.menu_dropdown_item, menuItem)

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
                    val intent = Intent(this@MailViewActivity,MailReplyActivity::class.java)
                    intent.putExtra("reply",mail)
                    startActivity(intent)
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")

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
                    dialog.show(supportFragmentManager, "CustomDialog")
                }
                //신고
                1 -> {
                    val intent = Intent(this,ReportActivity::class.java)
                    intent.putExtra("type",mail.type)
                    intent.putExtra("typeIdx",mail?.typeIdx)
                    startActivity(intent)
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
                                    blockService.setBlock(BlackList(mail?.typeIdx,mail?.senderIdx))
                                }
                                override fun onButton2Clicked() {
                                }
                            })
                            checkDialog.show(supportFragmentManager, "CustomDialog")
                        }
                    })
                    dialog.show(supportFragmentManager, "CustomDialog")
                }
            }
        }


        binding.letterViewToolbar.toolbarBackIc.setOnClickListener{
            finish()
        }
    }

    fun setFont(idx:Int){
        val fonts= resources.getStringArray(R.array.fontEng)
        val fontId= resources.getIdentifier(fonts[idx],"font", packageName)
        binding.mailViewBodyTv.typeface = ResourcesCompat.getFont(this,fontId)
        binding.mailViewDateTv.typeface = ResourcesCompat.getFont(this,fontId)
        binding.mailViewSenderTv.typeface= ResourcesCompat.getFont(this,fontId)
    }

    override fun onSetBlockViewSuccess(result: Int) {

    }

    override fun onSetBlockViewFailure(code: Int, message: String) {

    }


}
