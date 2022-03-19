package com.likefirst.btos.ui.archive

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.likefirst.btos.databinding.DialogArchiveListPeriodBinding
import com.likefirst.btos.ui.view.archive.ArchiveListFragment
import java.util.*

class ArchiveListPeriodDialog(): BottomSheetDialogFragment() {
    lateinit var binding: DialogArchiveListPeriodBinding

    companion object {
        fun newInstance() : ArchiveListPeriodDialog{
            return ArchiveListPeriodDialog()
        }
    }

    interface DatePickerClickListener{
        fun onDatePicked(dateFrom : String, dateTo: String)
    }

    private lateinit var mDatePickerClickListener : DatePickerClickListener

    fun setDatePickerClickListener(datePickerclickListener: DatePickerClickListener){
        mDatePickerClickListener = datePickerclickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogArchiveListPeriodBinding.inflate(inflater, container, false)

        initDatePickerSetting()
        initBtns()

        return binding.root
    }

    fun initDatePickerYear(){
        val calendar = GregorianCalendar.getInstance()
        calendar.add(Calendar.YEAR, -100)
        binding.archiveListPeriodStartYearNp.minValue = calendar.get(Calendar.YEAR)
        binding.archiveListPeriodEndYearNp.minValue = calendar.get(Calendar.YEAR)
        calendar.add(Calendar.YEAR, 200)
        binding.archiveListPeriodStartYearNp.maxValue = calendar.get(Calendar.YEAR)
        binding.archiveListPeriodEndYearNp.maxValue = calendar.get(Calendar.YEAR)
    }

    fun initDatePickerMonth(){
        binding.archiveListPeriodStartMonthNp.minValue = 1
        binding.archiveListPeriodStartMonthNp.maxValue = 12
        binding.archiveListPeriodEndMonthNp.minValue = 1
        binding.archiveListPeriodEndMonthNp.maxValue = 12
    }

    fun initDatePickerDayFrom(calendar: Calendar){
        binding.archiveListPeriodStartDayNp.minValue = 1
        binding.archiveListPeriodStartDayNp.maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun initDatePickerDayTo(calendar: Calendar){
        binding.archiveListPeriodEndDayNp.minValue = 1
        binding.archiveListPeriodEndDayNp.maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun initDatePickerSetting(){
        val calendar = GregorianCalendar.getInstance()
        val yearFrom = binding.archiveListPeriodStartYearNp
        val monthFrom = binding.archiveListPeriodStartMonthNp
        val dayFrom = binding.archiveListPeriodStartDayNp
        val yearTo = binding.archiveListPeriodEndYearNp
        val monthTo = binding.archiveListPeriodEndMonthNp
        val dayTo = binding.archiveListPeriodEndDayNp

        // 순환 안되게 막기
        yearFrom.wrapSelectorWheel = false
        monthFrom.wrapSelectorWheel = false
        dayFrom.wrapSelectorWheel = false
        yearTo.wrapSelectorWheel = false
        monthTo.wrapSelectorWheel = false
        dayTo.wrapSelectorWheel = false

        // 꾹 눌러서 editText 전환되는거 막기
        yearFrom.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthFrom.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        dayFrom.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        yearTo.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthTo.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        dayTo.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        initDatePickerYear()
        initDatePickerMonth()
        initDatePickerDayFrom(calendar)
        initDatePickerDayTo(calendar)

        // 오늘 날짜로 표시
        yearFrom.value = calendar.get(Calendar.YEAR)
        monthFrom.value = calendar.get(Calendar.MONTH) + 1
        dayFrom.value = calendar.get(Calendar.DAY_OF_MONTH)
        yearTo.value = calendar.get(Calendar.YEAR)
        monthTo.value = calendar.get(Calendar.MONTH) + 1
        dayTo.value = calendar.get(Calendar.DAY_OF_MONTH)

        binding.archiveListPeriodStartYearNp.setOnValueChangedListener{picker, oldVal, newVal ->
            calendar.set(Calendar.YEAR, newVal)
            initDatePickerDayFrom(calendar)
        }

        binding.archiveListPeriodStartMonthNp.setOnValueChangedListener { picker, oldVal, newVal ->
            calendar.set(Calendar.MONTH, newVal - 1)
            initDatePickerDayFrom(calendar)
        }

        binding.archiveListPeriodEndYearNp.setOnValueChangedListener{picker, oldVal, newVal ->
            calendar.set(Calendar.YEAR, newVal)
            initDatePickerDayTo(calendar)
        }

        binding.archiveListPeriodEndMonthNp.setOnValueChangedListener { picker, oldVal, newVal ->
            calendar.set(Calendar.MONTH, newVal - 1)
            initDatePickerDayTo(calendar)
        }
    }

    fun initBtns() {
        binding.archiveListPeriodCancelTv.setOnClickListener {
            dismiss()
        }
        binding.archiveListPeriodSubmitTv.setOnClickListener {
            val yearFrom = binding.archiveListPeriodStartYearNp.value
            val monthFrom = binding.archiveListPeriodStartMonthNp.value
            val dayFrom = binding.archiveListPeriodStartDayNp.value
            val yearTo = binding.archiveListPeriodEndYearNp.value
            val monthTo = binding.archiveListPeriodEndMonthNp.value
            val dayTo = binding.archiveListPeriodEndDayNp.value

            val dateFrom = "$yearFrom.${String.format("%02d", monthFrom)}.${String.format("%02d", dayFrom)}"
            val dateTo = "$yearTo.${String.format("%02d", monthTo)}.${String.format("%02d", dayTo)}"
            mDatePickerClickListener.onDatePicked(dateFrom, dateTo)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        ArchiveListFragment.datePickerFlag = true
        super.onDismiss(dialog)
    }

}