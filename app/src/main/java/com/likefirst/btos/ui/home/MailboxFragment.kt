package com.likefirst.btos.ui.home

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryInfo
import com.likefirst.btos.data.remote.response.Diary
import com.likefirst.btos.data.remote.posting.response.Letter
import com.likefirst.btos.data.remote.response.Mailbox
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.service.LetterService
import com.likefirst.btos.data.remote.posting.service.MailboxService
import com.likefirst.btos.data.remote.posting.view.DiaryView
import com.likefirst.btos.data.remote.posting.view.LetterView
import com.likefirst.btos.data.remote.posting.view.MailboxView
import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.posting.MailWriteActivity
import com.likefirst.btos.utils.toArrayList


class MailboxFragment : BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate),
    MailboxView, LetterView,
    DiaryView {



    override fun initAfterBinding() {
        val presFragment  = this
        val mailboxService= MailboxService()
        mailboxService.setMailboxView(this)
        mailboxService.loadMailbox("2")
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
                        letterService.loadLetter("letter","1")

                    }
                    "diary"->{
                        saveMail(mail)
                        val diaryService= DiaryService()
                        diaryService.setDiaryView(this@MailboxFragment)
                        diaryService.loadDiary("diary","1")
                    }
                }

            }
        })
    }

    fun saveMail(mail:Mailbox) {
        Log.d("Letter/API-MAIL",mail.toString())
        val spf= requireActivity().getSharedPreferences("MailBox",
            AppCompatActivity.MODE_PRIVATE)
        val editor=spf.edit()
        editor.putInt("Idx",mail.idx)
        editor.putString("senderName",mail.senderNickName)
        editor.putString("sendAt",mail.sendAt)
        editor.commit()
    }

    fun getDiary(diary: Diary){
        val spf= requireActivity().getSharedPreferences("MailBox",
            AppCompatActivity.MODE_PRIVATE)
        val id=spf.getInt("Idx",-1)
        val senderName=spf.getString("senderName","")
        val sendAt=spf.getString("sendAt","")

        Log.d("Letter/API-DIARY",id.toString())
        if(id!=-1){
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("mailbox")
                .commit()

            val doneList :List<String> = diary.doneList.map{donelist ->donelist.content}
            val Diary =DiaryInfo(diaryDate = sendAt, doneLists =doneList.toArrayList(),emotionIdx = diary.emotionIdx,contents = diary.content,userName=senderName!!)
            val  intent: Intent = Intent(requireContext()!!,DiaryViewerActivity::class.java)
            intent.putExtra("diaryInfo",Diary)
            requireActivity().startActivity(intent)
        }
    }


    fun getLetter(letter:Letter){
        val spf= requireActivity().getSharedPreferences("MailBox",
            AppCompatActivity.MODE_PRIVATE)
        val id=spf.getInt("Idx",-1)
        val senderName=spf.getString("senderName","")
        val sendAt=spf.getString("sendAt","")
        Log.d("Letter/API",id.toString())
        val Letter = letter
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

    override fun onLetterSuccess(letter:Letter) {
        Log.d("Letter/API : Success",letter.toString())
        getLetter(letter)
    }

    override fun onLetterFailure(code: Int, message: String) {
        Log.d("Letter/API : Fail",message.toString())
    }

    override fun onDiaryLoading() {
    }

    override fun onDiarySuccess(diary: Diary) {
        Log.d("Diary/API : Success",diary.toString())
        getDiary(diary)
    }

    override fun onDiaryFailure(code: Int, message: String) {
        Log.d("Diary/API : Fail", message.toString())

    }
}