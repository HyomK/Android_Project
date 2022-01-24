package com.likefirst.btos.ui.archive

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentArchiveCalendarBinding
import com.likefirst.btos.ui.BaseFragment
import java.lang.Math.abs
import java.util.*

class ArchiveCalendarFragment : BaseFragment<FragmentArchiveCalendarBinding>(FragmentArchiveCalendarBinding::inflate) {
    var pageIndex = 0
    lateinit var currentDate : Date
    val mCalendar: Calendar = GregorianCalendar.getInstance()
    override fun initAfterBinding() {

        setCalendar()
    }

    fun setCalendar(){
        val monthNames: Array<String> = resources.getStringArray(R.array.month)
        val MONTH_TODAY = mCalendar.get(Calendar.MONTH)
        val YEAR_TODAY = mCalendar.get(Calendar.YEAR)

        // 오늘 날짜 표시
        binding.archiveCalendarMonthTv.text = monthNames[MONTH_TODAY]
        binding.archiveCalendarYearTv.text = YEAR_TODAY.toString()

        val calendarAdapter = ArchiveCalendarVPAdapter(this)
        binding.archiveCalendarVp.apply {
            adapter = calendarAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setCurrentItem(Int.MAX_VALUE/2, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    //pageIndex 설정 (현재 달을 기준값 0으로 잡음)
                    val centerPosition = Int.MAX_VALUE/2
                    pageIndex = position - centerPosition

                    //현재 표시되는 달 텍스트 변환
                    if(MONTH_TODAY + pageIndex >= 0){
                        val monthIndex = (MONTH_TODAY + pageIndex) % 12
                        val year = (MONTH_TODAY + pageIndex) / 12 + YEAR_TODAY
                        binding.archiveCalendarMonthTv.text = monthNames[monthIndex]
                        binding.archiveCalendarYearTv.text = year.toString()
                    } else {
                        val monthIndexAbs = kotlin.math.abs(MONTH_TODAY + pageIndex)
                        val year = YEAR_TODAY - ((monthIndexAbs-1) / 12) - 1
                        val monthIndex = 12 - (monthIndexAbs % 12)
                        if (monthIndex == 12){
                            binding.archiveCalendarMonthTv.text = monthNames[0]
                        } else {
                            binding.archiveCalendarMonthTv.text = monthNames[monthIndex]
                        }
                        binding.archiveCalendarYearTv.text = year.toString()
                    }
                    super.onPageSelected(position)
                }
            })
        }
    }
//    fun initView() {
//        pageIndex -= (Int.MAX_VALUE / 2)
//        Log.d("pageIndex", (pageIndex%12).toString())
//        Log.e("Calendar Index", "Calender Index: $pageIndex")
//
//        val date = mCalendar.run {
//            add(GregorianCalendar.MONTH, pageIndex)
//            time
//        }
//        currentDate = date
//        val month = monthNames[date.]
//    }
}