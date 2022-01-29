package com.likefirst.btos.ui.home

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.MailWriteActivity

class MailboxFragment : BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate){


    override fun initAfterBinding() {
        val presFragment  = this
        setClickListener(presFragment )
        setMailView( presFragment )
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


    fun setMailView(presFragment :Fragment){
        val mActivity = activity as MainActivity
        val data = Array(20) { i -> "Number of index: $i"  }
        val adapter = MailRVAdapter(data)
        binding.mailboxRv.adapter= adapter


        adapter.setMyItemCLickLister(object: MailRVAdapter.MailItemClickListener {
            override fun onClickItem() {

                val frgmn = MailViewFragment()
                // 테이블에 읽은 편지를 표시할 수 있는 isWritten =true
                //body에 본문 내용을 서버에서 받아 넣음
                frgmn.arguments =bundleOf(
                    "body" to "mailtext"
                )
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.home_main_layout,frgmn,"viewmail")
                    .hide(presFragment)
                    .show(frgmn)
                    .addToBackStack(null)
                    .commit()

            }
        })
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


}