package com.likefirst.btos.presentation.View.archive

import android.icu.util.UniversalTimeScale
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


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