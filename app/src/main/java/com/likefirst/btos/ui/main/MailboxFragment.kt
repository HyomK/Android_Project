package com.likefirst.btos.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.likefirst.btos.R

import com.likefirst.btos.databinding.FragmentMailboxBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.utils.Inflate

class MailboxFragment : BaseFragment<FragmentMailboxBinding>(FragmentMailboxBinding::inflate){
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity
        binding.mailboxWriteBtn.setOnClickListener {
        }
        binding.mailboxBackBtn.setOnClickListener{
            Log.d("exit", "mailbox exit")

            mActivity.changeFragment(this).backHome()
        }



        val data = Array(20) { i -> "Number of index: $i"  }
        val adapter =MailRVAdapter(data)
        binding.mailboxRv.adapter= adapter

        adapter.setMyItemCLickLister(object:MailRVAdapter.MailItemClickListener{
            override fun onClickItem() {
                 val fr =MailViewFragment()
                 // 테이블에 읽은 편지를 표시할 수 있는 isWritten =true
                 //body에 본문 내용을 서버에서 받아 넣음
                 fr.arguments =bundleOf(
                     "body" to "mailtext"
                 )
                 mActivity.changeFragment(fr).moveFragment(R.id.fr_layout)

             }
          })





    }



}