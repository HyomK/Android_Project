package com.likefirst.btos.ui.archive

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemArchiveCalendarVpBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.posting.DiaryActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArchiveCalendarItemFragment(val pageIndex: Int) : BaseFragment<ItemArchiveCalendarVpBinding>(ItemArchiveCalendarVpBinding::inflate) {

    override fun initAfterBinding() {
        val calendarAdapter = ArchiveCalendarRVAdapter(createCalendarList(), requireContext())

        binding.archiveCalendarRv.apply {
            adapter = calendarAdapter
            layoutManager = GridLayoutManager(requireContext(), 7)
            val calendar = Calendar.getInstance()
            calendar.time = getCalendar()
            Log.d("calendar", calendar.toString())
            calendarAdapter.setYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH ) + 1)
            calendarAdapter.setOnDateSelectedListener(object : ArchiveCalendarRVAdapter.CalendarDateSelectedListener {
                override fun onDateSelectedListener(year: Int, month: Int, date: Int) {
                    Toast.makeText(requireContext(), "$year, $month, $date", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), DiaryActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }

    fun getCalendar() : Date{
        val calendar = Calendar.getInstance()
        val MONTH_TODAY = calendar.get(Calendar.MONTH)
        val YEAR_TODAY = calendar.get(Calendar.YEAR)

        if(MONTH_TODAY + pageIndex >= 0){
            val monthIndex = (MONTH_TODAY + pageIndex) % 12
            val year = (MONTH_TODAY + pageIndex) / 12 + YEAR_TODAY
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthIndex)
            return calendar.time
        } else {
            val monthIndexAbs = kotlin.math.abs(MONTH_TODAY + pageIndex)
            val year = YEAR_TODAY - ((monthIndexAbs-1) / 12) - 1
            val monthIndex = 12 - (monthIndexAbs % 12)
            if (monthIndex == 12){
                calendar.set(Calendar.MONTH, 0)
            } else {
                calendar.set(Calendar.MONTH, monthIndex)
            }
            calendar.set(Calendar.YEAR, year)
            return calendar.time
        }
    }

    fun createCalendarList() : ArrayList<Int>{
        val customCalendar = CustomCalendar(getCalendar())
        customCalendar.initBaseCalendar()
        return customCalendar.dateList
    }

}