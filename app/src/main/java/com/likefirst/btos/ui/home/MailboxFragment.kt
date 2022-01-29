package com.likefirst.btos.ui.home

import android.provider.CallLog
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.response.Letter
import com.likefirst.btos.data.remote.response.Mailbox
import com.likefirst.btos.data.remote.response.MailboxResponse
import com.likefirst.btos.data.remote.service.LetterService
import com.likefirst.btos.data.remote.service.MailboxService
import com.likefirst.btos.data.remote.view.LetterView
import com.likefirst.btos.data.remote.view.MailboxView
import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.MailWriteActivity


class MailboxFragment : BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate),MailboxView,LetterView{



    override fun initAfterBinding() {
        val presFragment  = this
        val mailboxService=MailboxService()
        mailboxService.setMailboxView(this)
        mailboxService.loadMailbox("1")
        setClickListener(presFragment)

    }


    override fun onStart() {
        super.onStart()
        val mActivity = activity as MainActivity
        mActivity.isDrawerOpen=false

    }

    override fun onPause() {
        super.onPause()
        Log.d("Mailbox","pause")
        val mActivity = activity as MainActivity
        mActivity.isMailOpen=false

    }


    fun setMailView(mailboxList: ArrayList<Mailbox>){
        val adapter = MailRVAdapter(mailboxList)
        binding.mailboxRv.adapter= adapter

        adapter.setMyItemCLickLister(object: MailRVAdapter.MailItemClickListener {
            override fun onClickItem(mail:Mailbox) {
                when(mail.type){
                    "letter"->{
                        saveMail(mail)
                        val letterService= LetterService()
                        letterService.setLetterView(this@MailboxFragment)
                        letterService.loadLetter("1")

                    }
                }

            }
        })
    }

    fun saveMail(mail:Mailbox) {
        val spf= requireActivity().getSharedPreferences("MailBox",
            AppCompatActivity.MODE_PRIVATE)
        val editor=spf.edit()
        editor.putInt("Idx",mail.idx)
        editor.putString("senderName",mail.senderNickName)
        editor.putString("sendAt",mail.sendAt)
    }


    fun getLetter(letterList: ArrayList<Letter>){
        val spf= requireActivity().getSharedPreferences("MailBox",
            AppCompatActivity.MODE_PRIVATE)

        val id=spf.getInt("Idx",-1)
        val senderName=spf.getString("senderName","")
        val sendAt=spf.getString("sendAt","")

        if(id!=-1){
            val Letter = letterList.find { letter -> letter.letterIdx ==id }
            if(Letter==null){
                Log.d("Letter", "찾기 실패")
                return
            }

            val frgmn = MailViewFragment()
            // 테이블에 읽은 편지를 표시할 수 있는 isWritten =true
            //body에 본문 내용을 서버에서 받아 넣음
            frgmn.arguments =bundleOf(
                "sender" to senderName,
                "date" to sendAt,
                "body" to Letter?.content.toString()
            )
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.home_main_layout,frgmn,"viewmail")
                .hide(this@MailboxFragment)
                .show(frgmn)
                .addToBackStack(null)
                .commit()

       }

    }



    fun setClickListener(presFragment: Fragment){

        val mActivity = activity as MainActivity
        binding.mailboxWriteBtn.setOnClickListener {

            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "편지를 쓸까요?\n편지를 보내면 랜덤한 사람에게 지금 바로 보낼 수 있습니다.",
                "btnData" to btn
            )
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked() {}
                override fun onButton2Clicked() {
                    mActivity.notifyDrawerHandler("lock")
                    mActivity.startNextActivity(MailWriteActivity::class.java)
                }
            })
            dialog.show(mActivity.supportFragmentManager, "CustomDialog")
        }

        binding.mailboxBackBtn.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()

        }
    }

    override fun onMailboxLoading() {

    }

    override fun onMailboxSuccess(mailboxList: ArrayList<Mailbox>) {
        Log.d("MailBox/API : Success",mailboxList.toString())
        setMailView(mailboxList)
    }

    override fun onMailboxFailure(code: Int, message: String) {
        Log.d("MailBox/API : Fail",message.toString())
    }

    override fun onLetterLoading() {

    }

    override fun onLetterSuccess(letterList: ArrayList<Letter>) {
        Log.d("Letter/API : Success",letterList.toString())
        getLetter(letterList)
    }

    override fun onLetterFailure(code: Int, message: String) {
        Log.d("Letter/API : Fail",message.toString())
    }


}