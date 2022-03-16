package com.likefirst.btos.presentation.View.archive

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.FragmentArchiveBinding
import com.likefirst.btos.presentation.BaseFragment

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(FragmentArchiveBinding::inflate) {
    val calendarPage = ArchiveCalendarFragment()
    val listPage = ArchiveListFragment()


    override fun initAfterBinding() {
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