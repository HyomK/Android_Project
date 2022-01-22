package com.likefirst.btos.ui.archive

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ArchiveFragmentVPAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    val fragmentList = arrayListOf<Fragment>(ArchiveCalendarFragment(), ArchiveCalendarFragment())

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}