package com.likefirst.btos.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.remote.posting.response.*
import com.likefirst.btos.data.remote.posting.service.MailboxService
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.data.remote.posting.view.MailboxView
import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.CustomDialogFragment
import com.likefirst.btos.ui.view.posting.DiaryViewerActivity
import com.likefirst.btos.ui.view.posting.MailWriteActivity
import com.likefirst.btos.data.remote.notify.viewmodel.NotifyViewModel
import com.likefirst.btos.data.remote.posting.viewmodel.MailViewModel
import com.likefirst.btos.data.remote.posting.viewmodel.MailViewModelFactory
import com.likefirst.btos.data.remote.posting.viewmodel.MailboxRepository
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.utils.getUserIdx


class MailboxFragment: BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate),
    MailboxView, MailLetterView,MailReplyView,
    MailDiaryView , MainActivity.onBackPressedListener{

    lateinit var  notifyViewModel : NotifyViewModel
    lateinit var mailViewModel : MailViewModel
    private val mailboxService= MailboxService()
    lateinit var mailBoxAdapter : MailRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mailViewModel=ViewModelProvider(requireActivity(),MailViewModelFactory(MailboxRepository())).get(MailViewModel::class.java)
        mailViewModel.mailList.observe(viewLifecycleOwner, Observer {
                it-> run {
                        Log.e("mailbox-init","$it")
                        mailBoxAdapter.initData(it)
                        mailBoxAdapter.notifyDataSetChanged()
                    }
        })
        mailViewModel.loadMailList(this, getUserIdx())

        notifyViewModel= ViewModelProvider(requireActivity()).get(NotifyViewModel::class.java)
        notifyViewModel.getMsgLiveData().observe(viewLifecycleOwner, Observer<Boolean>{
            if(it){
                Log.e("notify-init","$it")
                mailViewModel.loadMailList(this, getUserIdx())
                mailBoxAdapter.initData(mailViewModel.mailList.value!!)
                mailBoxAdapter.notifyDataSetChanged()
                notifyViewModel.setMsgLiveData(false)
            }
        })
        initAfterBinding()
    }

    override fun initAfterBinding() {
        initRecyclerView()
        setClickListener()
    }

    fun initRecyclerView(){
        mailBoxAdapter= MailRVAdapter()
        binding.mailboxRv.adapter=mailBoxAdapter
    }


    override fun onStart() {
        super.onStart()
        Log.d("Mailbox","start")
        val mActivity = activity as MainActivity
        mActivity.isDrawerOpen=false
        mActivity.isMailOpen=true
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("Mailbox","destroy")
        val mActivity = activity as MainActivity
        mActivity.isMailOpen=false
    }


    override fun onResume() {
        super.onResume()
        val mActivity = activity as MainActivity
        mActivity.isMailOpen=true
    }


    override fun onBackPressed() {
        val mActivity = activity as MainActivity
        if(!mActivity.isMailOpen) mActivity.isMailOpen= true
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }


    fun getDiary(diary: MailInfoResponse){

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
            if(!mActivity.isMailOpen) mActivity.isMailOpen= true
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            mActivity.isMailOpen=false
        }


        mailBoxAdapter.setMyItemCLickLister(object: MailRVAdapter.MailItemClickListener {
            override fun onClickItem(mail:Mailbox, position: Int) {

                when(mail.type){
                    "letter"->{
                        mailViewModel.loadLetter(this@MailboxFragment, getUserIdx(),mail.idx)
                    }
                    "diary"->{
                        mailViewModel.loadDiary(this@MailboxFragment, getUserIdx(),mail.idx)
                    }
                    "reply"->{
                        mailViewModel.loadReply(this@MailboxFragment, getUserIdx(),mail.idx)
                    }
                }
                mActivity.notifyDrawerHandler("unlock")
                mailBoxAdapter.removeItem(position)
            }
        })
    }

    override fun onMailboxLoading() {
    }

    override fun onMailboxSuccess(mailboxList: ArrayList<Mailbox>) {
        binding.setMailboxLoadingPb.visibility= View.GONE
        Log.d("MailBox/API : Success",mailboxList.toString())

    }

    override fun onMailboxFailure(code: Int, message: String) {
        Log.d("MailBox/API : Fail",message.toString())
        binding.setMailboxLoadingPb.visibility= View.GONE
    }

    override fun onLetterLoading() {
        setLoadingView()
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
        setLoadingView()
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
        setLoadingView()
    }

    override fun onReplySuccess(reply: MailInfoResponse){
        binding.setMailboxLoadingPb.visibility= View.GONE
        getMail(reply)
    }

    override fun onReplyFailure(code: Int, message: String) {
        Log.e("ReplyAPI",message)
        binding.setMailboxLoadingPb.visibility= View.GONE
    }


    fun setLoadingView(){
        binding.setMailboxLoadingPb.visibility= View.VISIBLE
        binding.setMailboxLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }



}
