package com.likefirst.btos.ui.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoryFragStateAdapter (fragment : Fragment) : FragmentStateAdapter(fragment)  {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HistoryBasicFragment("sender")
            1-> HistoryBasicFragment("diary")
            2-> HistoryBasicFragment("letter")
            else -> HistoryBasicFragment("sender")
        }
    }
}