package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityNoticeBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.profile.setting.NoticeRVAdapter

class NoticeActivity: BaseActivity<ActivityNoticeBinding>(ActivityNoticeBinding::inflate),
    MainActivity.onBackPressedListener  {
    override fun initAfterBinding() {
        binding.profileNoticeToolbar.toolbarTitleTv.text="공지사항"


        binding.profileNoticeToolbar.toolbarBackIc.setOnClickListener {
            finish()
        }



        val dummyBody = resources.getString(R.string.profile_notice)
        val dummyDate="2022.01.29"
        var noticeList : ArrayList<Pair<String,String>> = ArrayList<Pair<String, String>>(10)//body , date
        for (i in 1..10){
            noticeList.add(Pair(i.toString()+ dummyBody, dummyDate))
        }

        val noticeAdapter=NoticeRVAdapter(noticeList)
        binding.profileNoticeRv.adapter=noticeAdapter


    }

}