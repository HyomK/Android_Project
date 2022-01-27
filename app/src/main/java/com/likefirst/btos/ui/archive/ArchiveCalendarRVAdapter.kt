package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemArchiveCalendarRvDateBinding
import com.likefirst.btos.databinding.ItemArchiveCalendarRvEmptyBinding
import java.lang.RuntimeException

class ArchiveCalendarRVAdapter(val calendarList : ArrayList<Int>, val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EMPTY_CELL = 0
    private val DATE_CELL = 1
    private var year = 0
    private var month = 0

    inner class DateViewHolder(val binding : ItemArchiveCalendarRvDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(position: Int){
            binding.itemArchiveCalendarTv.text = calendarList[position].toString()
            if (position % 7 == 0){
                val calendarRed = ContextCompat.getColor(context, R.color.notice_red)
                binding.itemArchiveCalendarTv.setTextColor(calendarRed)
            }
        }
    }

    interface CalendarDateSelectedListener{
        fun onDateSelectedListener(year : Int, month: Int, date: Int)
    }

    private lateinit var mCalendarDateSelectedListener: CalendarDateSelectedListener

    fun setOnDateSelectedListener(calendarDateSelectedListener: CalendarDateSelectedListener){
        mCalendarDateSelectedListener = calendarDateSelectedListener
    }

    inner class EmptyViewHolder(val binding : ItemArchiveCalendarRvEmptyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(){

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(calendarList[position] == 0) EMPTY_CELL else DATE_CELL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            EMPTY_CELL -> {
                val binding = ItemArchiveCalendarRvEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(binding)
            }
            DATE_CELL -> {
                val binding = ItemArchiveCalendarRvDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
            }
            else -> {
                throw RuntimeException("Unknown ViewType Error in ArchiveCalendarRVAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyViewHolder){
            holder.initView()
        }
        else if (holder is DateViewHolder){
            holder.initView(position)
            holder.itemView.setOnClickListener {
                mCalendarDateSelectedListener.onDateSelectedListener(year, month, calendarList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return 42
    }

    fun setYearMonth(year: Int, month: Int){
        this.year = year
        this.month = month
    }
}