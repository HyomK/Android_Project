package com.likefirst.btos.ui.archive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.FragmentArchiveBinding
import com.likefirst.btos.ui.BaseFragment

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(FragmentArchiveBinding::inflate) {
    override fun initAfterBinding() {
        val calendarPage = ArchiveCalendarFragment()
        val listPage = ArchiveListFragment()
        val archivePageList = arrayListOf<Fragment>(calendarPage, listPage)
        val archiveAdapter = ArchiveFragmentVPAdapter(this, archivePageList)
        binding.archiveVp.apply {
            adapter = archiveAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            isSaveEnabled = false
            offscreenPageLimit = 2   // 뷰페이저 이전상태 유지하는 페이지 최대 개수
        }
    }
}