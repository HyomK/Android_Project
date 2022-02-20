package com.likefirst.btos.ui.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.likefirst.btos.databinding.ItemArchiveCalendarVpBinding

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