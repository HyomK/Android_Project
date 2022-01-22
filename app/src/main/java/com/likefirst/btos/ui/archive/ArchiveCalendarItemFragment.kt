package com.likefirst.btos.ui.archive

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.databinding.ItemArchiveCalendarVpBinding
import com.likefirst.btos.ui.BaseFragment
import java.util.*

class ArchiveCalendarItemFragment(val position: Int) : BaseFragment<ItemArchiveCalendarVpBinding>(ItemArchiveCalendarVpBinding::inflate) {

    override fun initAfterBinding() {
        val calendarAdapter = ArchiveCalendarRVAdapter()
        binding.testtv.text = position.toString()
//        binding.archiveCalendarRv.apply {
//            adapter = calendarAdapter
//            layoutManager = GridLayoutManager(requireContext(), 7)
//        }
    }

}