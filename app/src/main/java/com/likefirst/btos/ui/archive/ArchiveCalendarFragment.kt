package com.likefirst.btos.ui.archive

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.FragmentArchiveCalendarBinding
import com.likefirst.btos.ui.BaseFragment
import java.util.*

class ArchiveCalendarFragment : BaseFragment<FragmentArchiveCalendarBinding>(FragmentArchiveCalendarBinding::inflate) {
    var pageIndex = 0
    lateinit var currentDate : Date
    val mCalendar: Calendar = GregorianCalendar.getInstance()
    val monthNames = arrayListOf("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER")
    override fun initAfterBinding() {
        //오늘 월 표시
        binding.archiveCalendarTv.text = monthNames[mCalendar.get(Calendar.MONTH)]

        val calendarAdapter = ArchiveCalendarVPAdapter(this)
        binding.archiveCalendarVp.apply {
            adapter = calendarAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            currentItem = Int.MAX_VALUE/2
        }

        fun initView() {
            pageIndex -= (Int.MAX_VALUE / 2)
            Log.d("pageIndex", (pageIndex%12).toString())
            Log.e("Calendar Index", "Calender Index: $pageIndex")

            val date = mCalendar.run {
                add(GregorianCalendar.MONTH, pageIndex)
                time
            }
            currentDate = date
//        val month = monthNames[date.]
        }
    }
}