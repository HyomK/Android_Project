package com.likefirst.btos.ui.archive

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.data.remote.viewer.response.ArchiveCalendar
import com.likefirst.btos.data.remote.viewer.service.ArchiveCalendarService
import com.likefirst.btos.data.remote.viewer.view.ArchiveCalendarView
import com.likefirst.btos.databinding.ItemArchiveCalendarVpBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.posting.DiaryActivity
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.dateToStringMonth
import com.likefirst.btos.utils.getUserIdx
import java.util.*
import kotlin.collections.ArrayList

class ArchiveCalendarItemFragment(val pageIndex: Int, val viewMode: Int) : BaseFragment<ItemArchiveCalendarVpBinding>(ItemArchiveCalendarVpBinding::inflate)
, ArchiveCalendarView{

    override fun initAfterBinding() {

        createDiaryList()
    }

    fun createDiaryList(){
        var type = ""
        when (viewMode){
            0 -> type = "doneList"
            1 -> type = "emotion"
        }
        Log.d("viewMode", viewMode.toString())
        val date = dateToStringMonth(getCalendar())
        val archiveCalendarService = ArchiveCalendarService()
        archiveCalendarService.setArchiveCalendarView(this)
        archiveCalendarService.getCalendar(getUserIdx(), date, type)
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

    fun createCalendarList(calendarInputList : ArrayList<ArchiveCalendar>) : ArrayList<ArchiveCalendar> {
        val customCalendar = CustomCalendar(getCalendar(), calendarInputList)
        customCalendar.initBaseCalendar()
        Log.d("calendarList", customCalendar.dateList.toString())
        return customCalendar.dateList
    }

    override fun onArchiveCalendarLoading() {
        //TODO : 로딩화면 처리
    }

    override fun onArchiveCalendarSuccess(result: ArrayList<ArchiveCalendar>) {

        val calendarAdapter = ArchiveCalendarRVAdapter(createCalendarList(result), requireContext(), viewMode)

        binding.archiveCalendarRv.apply {
            adapter = calendarAdapter
            layoutManager = GridLayoutManager(requireContext(), 7)
            calendarAdapter.setDate(getCalendar())
            calendarAdapter.setOnDateSelectedListener(object : ArchiveCalendarRVAdapter.CalendarDateSelectedListener {
                override fun onDateSelectedListener(date: Date, dayInt : Int) {
                    val calendar = GregorianCalendar.getInstance()
                    calendar.time = date
                    calendar.set(Calendar.DAY_OF_MONTH, dayInt)

                    // 오늘 날짜랑 비교 (1. calendar = GregorianCalendar.getInstance() : 0
                    //                   2. calendar > GregorianCalendar.getInstance() : 1
                    //                   3. calendar < GregorianCalendar.getInstance() : -1)
                    if(calendar.compareTo(GregorianCalendar.getInstance()) == 1){
                        // TODO: Toast는 커스텀이 deprecated 되었기 때문에 SnackBar를 이용해서 커스텀 진행
                        Snackbar.make(view!!, "미래의 일기는 작성할 수 없어요!!!", Snackbar.LENGTH_SHORT).show()
                    } else {
                        val dateString = dateToString(calendar.time)
                        val intent = Intent(requireContext(), DiaryActivity::class.java)
                        intent.putExtra("diaryDate", dateString)
                        startActivity(intent)
                    }
                }

                override fun onDiarySelectedListener() {
                    // TODO: 일기조회 API연결
                }
            })
        }
    }

    override fun onArchiveCalendarFailure(code : Int) {
        when (code){
            // TODO: 4000번 에러 뜨는 이유 물어보기
            4000 -> Snackbar.make(requireView(), "일기를 불러오는데 실패하였습니다. 다시 시도해 주세요", Snackbar.LENGTH_SHORT).show()
            6004 -> Snackbar.make(requireView(), "프리미엄 계정 가입이 필요합니다.", Snackbar.LENGTH_SHORT).show()
        }
    }
}