package com.likefirst.btos.ui.profile.setting

import com.likefirst.btos.R
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.data.remote.notify.service.NoticeService
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.databinding.ActivityNoticeBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.profile.setting.NoticeRVAdapter

class NoticeActivity: BaseActivity<ActivityNoticeBinding>(ActivityNoticeBinding::inflate),NoticeAPIView {
    override fun initAfterBinding() {
        binding.profileNoticeToolbar.toolbarTitleTv.text="공지사항"

        binding.profileNoticeToolbar.toolbarBackIc.setOnClickListener {
            finish()
        }
        val noticeService = NoticeService()
        noticeService.setNoticeView(this)
        noticeService.loadNotice()

    }

    override fun onNoticeAPIError(Dialog: CustomDialogFragment) {
        Dialog.show(supportFragmentManager,"NoticeError")
    }

    override fun onNoticeAPISuccess(noticeList: ArrayList<NoticeDetailResponse>) {
        var noticeArray = ArrayList<Pair<NoticeDetailResponse, Boolean>>()//body , date
        noticeList.sortByDescending { it ->it.noticeIdx}
        noticeList.map{ i-> noticeArray.add(Pair(i,false))}
        val noticeAdapter=NoticeRVAdapter(noticeArray)
        binding.profileNoticeRv.adapter=noticeAdapter


    }

    override fun onNoticeAPIFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }

}