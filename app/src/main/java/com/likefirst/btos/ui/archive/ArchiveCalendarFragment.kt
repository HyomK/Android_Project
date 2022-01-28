package com.likefirst.btos.ui.archive

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentArchiveCalendarBinding
import com.likefirst.btos.ui.BaseFragment
import java.time.Year
import java.util.*

class ArchiveCalendarFragment : BaseFragment<FragmentArchiveCalendarBinding>(FragmentArchiveCalendarBinding::inflate){

    var pageIndex = 0
    lateinit var currentDate : Date
    val mCalendar: Calendar = GregorianCalendar.getInstance()
    override fun initAfterBinding() {

        initCalendar(0)
        setDatePicker()
    }

    fun initCalendar(viewMode : Int){
        val monthNames: Array<String> = resources.getStringArray(R.array.month)
        val MONTH_TODAY = mCalendar.get(Calendar.MONTH)
        val YEAR_TODAY = mCalendar.get(Calendar.YEAR)

        // 오늘 날짜 표시
        binding.archiveCalendarMonthTv.text = monthNames[MONTH_TODAY]
        binding.archiveCalendarYearTv.text = YEAR_TODAY.toString()

        val calendarAdapter = ArchiveCalendarVPAdapter(this, viewMode)
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
        setCalendarRadioBtn()
    }

    fun setCalendar(year: Int, month: Int){
        val monthNames: Array<String> = resources.getStringArray(R.array.month)
        val oldCalendar = Calendar.getInstance()

        // mCalendar를 newCalendar로 설정
        mCalendar.set(year, month - 1, 1)

        // 옮겨야 할 postion 계산
        val yearGap = mCalendar.get(Calendar.YEAR) - oldCalendar.get(Calendar.YEAR)
        val monthGap = mCalendar.get(Calendar.MONTH) - oldCalendar.get(Calendar.MONTH)
        val position = (Int.MAX_VALUE/2 + (yearGap*12) + monthGap)

        // 뷰 그려주기
        binding.archiveCalendarVp.setCurrentItem(position, false)
        binding.archiveCalendarYearTv.text = year.toString()
        binding.archiveCalendarMonthTv.text = monthNames[month - 1]
    }

    fun setDatePicker(){
        binding.archiveCalendarDateLayout.setOnClickListener{
            val year = Integer.parseInt(binding.archiveCalendarYearTv.text.toString())
            val monthText = binding.archiveCalendarMonthTv.text.toString()
            val monthNames: Array<String> = resources.getStringArray(R.array.month)
            val month = monthNames.indexOf(monthText) + 1

            val datePickerDialog = ArchiveCalendarPeriodDialog.newInstance(year, month)
            datePickerDialog.setDatePickerClickListener(object : ArchiveCalendarPeriodDialog.DatePickerClickListener{
                override fun onDatePicked(year: Int, month: Int) {
                    setCalendar(year, month)
                }
            })
            datePickerDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
            datePickerDialog.show(childFragmentManager, datePickerDialog.tag)
        }
    }

    fun setCalendarRadioBtn(){
        binding.archiveCalendarRg.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId){
                R.id.archive_calendar_done_list_rb -> {
                    initCalendar(0)
                }
                R.id.archive_calendar_emotion_rb -> {
                    initCalendar(1)
                }
            }
        }
    }
}