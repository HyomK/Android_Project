//package com.likefirst.btos.ui.history
//
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.adapter.FragmentStateAdapter
//
//class HistoryFragStateAdapter (fragment : Fragment) : FragmentStateAdapter(fragment)  {
//    override fun getItemCount(): Int = 3
//
//    override fun createFragment(position: Int): Fragment {
//        return when(position){
//            0-> HistoryBasicFragment()
//            1-> HistoryBasicFragment()
//            2-> HistoryBasicFragment()
//            else -> HistoryBasicFragment()
//        }
//    }
//}