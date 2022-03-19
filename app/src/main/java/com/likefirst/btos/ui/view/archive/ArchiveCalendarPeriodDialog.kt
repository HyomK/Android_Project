package com.likefirst.btos.ui.view.archive

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.likefirst.btos.databinding.DialogArchiveCalendarPeriodBinding
import java.util.*



class ArchiveCalendarPeriodDialog(val year: Int, val month: Int): BottomSheetDialogFragment() {
    lateinit var binding: DialogArchiveCalendarPeriodBinding

    companion object{
        fun newInstance(year: Int, month: Int) : ArchiveCalendarPeriodDialog {
            return ArchiveCalendarPeriodDialog(year, month)
        }
    }

    interface DatePickerClickListener{
        fun onDatePicked(year : Int, month: Int)
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
        binding = DialogArchiveCalendarPeriodBinding.inflate(inflater, container, false)

        initDatePickerSetting()
        initBtns()

        return binding.root
    }

    fun initDatePickerYear(){
        val calendar = GregorianCalendar.getInstance()
        calendar.add(Calendar.YEAR, -100)
        binding.archiveCalendarYearNp.minValue = calendar.get(Calendar.YEAR)
        calendar.add(Calendar.YEAR, 200)
        binding.archiveCalendarYearNp.maxValue = calendar.get(Calendar.YEAR)
    }

    fun initDatePickerMonth(){
        binding.archiveCalendarMonthNp.minValue = 1
        binding.archiveCalendarMonthNp.maxValue = 12
    }

    fun initDatePickerSetting(){
        val calendar = GregorianCalendar.getInstance()
        val yearNp = binding.archiveCalendarYearNp
        val monthNp = binding.archiveCalendarMonthNp

        // 순환 안되게 막기
        yearNp.wrapSelectorWheel = false
        monthNp.wrapSelectorWheel = false

        // 꾹 눌러서 editText 전환되는거 막기
        yearNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        initDatePickerYear()
        initDatePickerMonth()

        yearNp.value = year
        monthNp.value = month
    }

    fun initBtns(){
        binding.archiveCalendarPeriodCancelTv.setOnClickListener {
            dismiss()
        }
        binding.archiveCalendarPeriodSubmitTv.setOnClickListener {
            val year = binding.archiveCalendarYearNp.value
            val month = binding.archiveCalendarMonthNp.value

            mDatePickerClickListener.onDatePicked(year, month)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        ArchiveCalendarFragment.datePickerFlag = true
        super.onDismiss(dialog)
    }
}