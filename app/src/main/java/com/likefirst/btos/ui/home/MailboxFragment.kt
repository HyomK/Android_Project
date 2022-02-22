package com.likefirst.btos.ui.home

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.posting.response.*
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.service.MailLetterService
import com.likefirst.btos.data.remote.posting.service.MailReplyService
import com.likefirst.btos.data.remote.posting.service.MailboxService
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.data.remote.posting.view.MailboxView
import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.posting.MailReplyActivity
import com.likefirst.btos.ui.posting.MailWriteActivity
import com.likefirst.btos.utils.toArrayList


class MailboxFragment: BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate),
    MailboxView, MailLetterView,MailReplyView,
    MailDiaryView , MainActivity.onBackPressedListener{

    override fun initAfterBinding() {
        val userDao = UserDatabase.getInstance(requireContext())!!.userDao()
        val userID= userDao.getUser()!!.userIdx!!
        setClickListener()
        val mailboxService= MailboxService()
        mailboxService.setMailboxView(this)
        mailboxService.loadMailbox(userID)

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
        val userDao = UserDatabase.getInstance(requireContext())!!.userDao()
        val userID= userDao.getUser()!!.userIdx!!
        adapter.setMyItemCLickLister(object: MailRVAdapter.MailItemClickListener {
            override fun onClickItem(mail:Mailbox) {
                when(mail.type){
                    "letter"->{
                        saveMail(mail)
                        val letterService= MailLetterService()
                        letterService.setLetterView(this@MailboxFragment)
                        letterService.loadLetter(userID,"letter",mail.idx)
                    }
                    "diary"->{
                        saveMail(mail)
                        val diaryService= DiaryService()
                        diaryService.setDiaryView(this@MailboxFragment)
                        diaryService.loadDiary(userID,"diary",mail.idx)
                    }
                    "reply"->{
                        saveMail(mail)
                        val replyService= MailReplyService()
                        replyService.setReplyView(this@MailboxFragment)
                        replyService.loadReply(userID,"reply",mail.idx)
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
        editor.putString("sendAt",mail.sendAt)
        editor.commit()
    }

    fun getDiary(diary: MailInfoResponse){
        var name : String="(알 수 없음)"
        if(diary.senderNickName !=null)
            name=diary.senderNickName
        val Diary = DiaryViewerInfo( diary.senderNickName, diary.emotionIdx, diary.sendAt, diary.content!!, true, diary.doneList!!)
        val  intent: Intent = Intent(requireContext(),DiaryViewerActivity::class.java)
        intent.putExtra("diaryInfo",Diary)
        requireActivity().startActivity(intent)
    }


    fun getMail(mail:MailInfoResponse){
        val bundle =bundleOf("mail" to mail)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .commit()

        val intent = Intent(context, MailViewActivity::class.java)
        intent.putExtra("MailView",bundle)
        startActivity(intent)
    }


    fun setClickListener(){
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
            mActivity.isMailOpen=false
            mActivity.supportFragmentManager.popBackStack()
        }
    }

    override fun onMailboxLoading() {
        binding.setMailboxLoadingPb.visibility= View.VISIBLE
    }

    override fun onMailboxSuccess(mailboxList: ArrayList<Mailbox>) {
        binding.setMailboxLoadingPb.visibility= View.GONE
        Log.d("MailBox/API : Success",mailboxList.toString())
        setMailView(mailboxList)
    }

    override fun onMailboxFailure(code: Int, message: String) {
        Log.d("MailBox/API : Fail",message.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE
    }

    override fun onLetterLoading() {
        binding.setMailboxLoadingPb.visibility= View.VISIBLE
    }

    override fun onLetterSuccess(letter: MailInfoResponse) {
        Log.d("Letter/API : Success",letter.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE
        getMail(letter)
    }


    override fun onLetterFailure(code: Int, message: String) {
        Log.d("Letter/API : Fail",message.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE
    }

    override fun onDiaryLoading() {
        binding.setMailboxLoadingPb.visibility= View.VISIBLE
    }

    override fun onDiarySuccess(diary:MailInfoResponse) {
        Log.d("Diary/API : Success",diary.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE
        getDiary(diary)
    }

    override fun onDiaryFailure(code: Int, message: String) {
        Log.d("Diary/API : Fail", message.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE

    }

    override fun onReplyLoading() {
        binding.setMailboxLoadingPb.visibility= View.VISIBLE
    }

    override fun onReplySuccess(reply: MailInfoResponse){
        binding.setMailboxLoadingPb.visibility= View.GONE
        getMail(reply)
    }

    override fun onReplyFailure(code: Int, message: String) {
        Log.e("ReplyAPI",message)
        binding.setMailboxLoadingPb.visibility= View.GONE
    }

    override fun onBackPressed() {
        val mActivity = activity as MainActivity
        mActivity.isMailOpen=false
        mActivity.supportFragmentManager.popBackStack()
    }

    override fun onResume() {
        super.onResume()
        val mActivity = activity as MainActivity
        mActivity.isMailOpen=true
    }

}
