package com.likefirst.btos.presentation.view.archive

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ArchiveFragmentVPAdapter(fragment : Fragment, val fragmentList : ArrayList<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}