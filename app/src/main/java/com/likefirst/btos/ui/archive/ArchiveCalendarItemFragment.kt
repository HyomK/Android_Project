package com.likefirst.btos.ui.archive

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.CalendarInfo
import com.likefirst.btos.databinding.ItemArchiveCalendarVpBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.posting.DiaryActivity
import com.likefirst.btos.utils.Converters
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.stringToDate
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArchiveCalendarItemFragment(val pageIndex: Int, val viewMode: Int) : BaseFragment<ItemArchiveCalendarVpBinding>(ItemArchiveCalendarVpBinding::inflate) {

    companion object{
        var viewMode = 0
    }

    override fun initAfterBinding() {
        val dummyData = ArrayList<CalendarInfo>()
        dummyData.add(CalendarInfo("2022.01.01", 0,1, 3))
        dummyData.add(CalendarInfo("2022.01.05", 0,5, 1))
        dummyData.add(CalendarInfo("2022.01.06", 0,10, 7))
        dummyData.add(CalendarInfo("2022.01.07", 0,1, 8))
        dummyData.add(CalendarInfo("2022.01.16", 0,3, 4))
        dummyData.add(CalendarInfo("2022.01.18", 0,2, 8))
        dummyData.add(CalendarInfo("2022.01.22", 0,6, 3))
        dummyData.add(CalendarInfo("2022.01.26", 0,7, 2))
        dummyData.add(CalendarInfo("2022.01.27", 0,7, 1))

        val calendarAdapter = ArchiveCalendarRVAdapter(createCalendarList(dummyData), requireContext(), viewMode)

        binding.archiveCalendarRv.apply {
            adapter = calendarAdapter
            layoutManager = GridLayoutManager(requireContext(), 7)
            val calendar = Calendar.getInstance()
            calendar.time = getCalendar()
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

    fun createCalendarList(calendarInputList : ArrayList<CalendarInfo>) : ArrayList<CalendarInfo> {
        val customCalendar = CustomCalendar(getCalendar(), calendarInputList)
        customCalendar.initBaseCalendar()
        Log.d("calendarList", customCalendar.dateList.toString())
        return customCalendar.dateList
    }
}