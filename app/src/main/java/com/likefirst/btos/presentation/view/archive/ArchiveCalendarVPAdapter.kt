package com.likefirst.btos.presentation.view.archive

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ArchiveCalendarVPAdapter(fragment: Fragment, val viewMode : Int): FragmentStateAdapter(fragment) {

    val centerPosition = Int.MAX_VALUE/2

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val pageIndex = position - centerPosition
        val bundle = Bundle()
        bundle.putInt("pageIndex", pageIndex)
        bundle.putInt("viewMode", viewMode)
        val calendarItemFragment = ArchiveCalendarItemFragment()
        calendarItemFragment.arguments = bundle
        return calendarItemFragment

    }
}