package com.likefirst.btos.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMailViewBinding

import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.main.ReportFragment
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.posting.MailReplyActivity
import com.likefirst.btos.ui.posting.MailWriteActivity
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.toArrayList


class MailViewActivity : BaseActivity<ActivityMailViewBinding>(ActivityMailViewBinding::inflate) {

    override fun initAfterBinding() {
        val bundle : Bundle = intent.getBundleExtra("MailView")!!

        binding.mailViewBodyTv.text= bundle.getString("body")
        binding.mailViewDateTv.text=bundle.getString("date")
        binding.mailViewSenderTv.text=bundle.getString("sender")

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
                    startNextActivity(MailReplyActivity::class.java)
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
                    supportFragmentManager.commit {
                        add(R.id.home_main_layout, ReportFragment())
                        addToBackStack("")
                    }
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
                            checkDialog.show(supportFragmentManager, "CustomDialog")
                        }
                    })
                    dialog.show(supportFragmentManager, "CustomDialog")
                }
            }
        }


        binding.letterViewToolbar.toolbarBackIc.setOnClickListener{
            supportFragmentManager.popBackStack()
        }
    }

}
