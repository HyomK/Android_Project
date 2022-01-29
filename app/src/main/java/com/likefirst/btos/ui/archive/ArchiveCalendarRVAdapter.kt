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
import com.likefirst.btos.data.entities.CalendarInfo
import com.likefirst.btos.databinding.ItemArchiveCalendarRvDateBinding
import com.likefirst.btos.databinding.ItemArchiveCalendarRvEmptyBinding
import com.likefirst.btos.databinding.ItemArchiveCalendarRvIconBinding
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class ArchiveCalendarRVAdapter(val calendarList : ArrayList<CalendarInfo>, val context : Context, val viewMode: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EMPTY_CELL = 0
    private val DATE_CELL = 1
    private val LEAF_CELL = 2
    private val EMOTION_CELL = 3
    private val DONELIST_MODE = 0
    private val EMOTION_MODE = 1
    private var ViewMode = 0
    private lateinit var date : Date

    inner class DateViewHolder(val binding : ItemArchiveCalendarRvDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(position: Int){
            binding.itemArchiveCalendarTv.text = calendarList[position].dateInt.toString()
            if (position % 7 == 0){
                val calendarRed = ContextCompat.getColor(context, R.color.notice_red)
                binding.itemArchiveCalendarTv.setTextColor(calendarRed)
            }
        }
    }

    inner class EmptyViewHolder(val binding : ItemArchiveCalendarRvEmptyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(){

        }
    }

    inner class LeafViewHolder(val binding : ItemArchiveCalendarRvIconBinding) : RecyclerView.ViewHolder(binding.root){
        fun initView(position: Int){
            val doneListNum = calendarList[position].doneListNum!!
            var leafImg = 0
            if(doneListNum in 0..2){
                leafImg = context.resources.getIdentifier("leaf1", "drawable", context.packageName)
            } else if (doneListNum in 3..4){
                leafImg = context.resources.getIdentifier("leaf2", "drawable", context.packageName)
            } else if (doneListNum in 5..6){
                leafImg = context.resources.getIdentifier("leaf3", "drawable", context.packageName)
            } else if (doneListNum in 7..8){
                leafImg = context.resources.getIdentifier("leaf4", "drawable", context.packageName)
            } else if (doneListNum in 9..10) {
                leafImg = context.resources.getIdentifier("leaf5", "drawable", context.packageName)
            }
            binding.itemArchiveCalendarIconIv.setImageResource(leafImg)
        }
    }

    inner class EmotionViewHolder(val binding : ItemArchiveCalendarRvIconBinding) : RecyclerView.ViewHolder(binding.root){
        fun initView(position: Int){
            val emotionIdx = calendarList[position].emotionIdx
            val emotionImg = context.resources.getIdentifier("emotion$emotionIdx", "drawable", context.packageName)
            binding.itemArchiveCalendarIconIv.setImageResource(emotionImg)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (calendarList[position].diaryDate == null) {
            if (calendarList[position].dateInt == 0) {
                EMPTY_CELL
            } else {
                DATE_CELL
            }
        } else {
            if (viewMode == DONELIST_MODE) {
                LEAF_CELL
            } else {
                EMOTION_CELL
            }
        }
    }

    interface CalendarDateSelectedListener{
        fun onDateSelectedListener(date : Date, dayInt: Int)
    }

    private lateinit var mCalendarDateSelectedListener: CalendarDateSelectedListener

    fun setOnDateSelectedListener(calendarDateSelectedListener: CalendarDateSelectedListener){
        mCalendarDateSelectedListener = calendarDateSelectedListener
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
            LEAF_CELL -> {
                val binding = ItemArchiveCalendarRvIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LeafViewHolder(binding)
            }
            EMOTION_CELL -> {
                val binding = ItemArchiveCalendarRvIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmotionViewHolder(binding)
            }
            else -> {
                throw RuntimeException("Unknown ViewType Error in ArchiveCalendarRVAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> {
                holder.initView()
            }
            is DateViewHolder -> {
                holder.initView(position)
                holder.itemView.setOnClickListener {
                    mCalendarDateSelectedListener.onDateSelectedListener(date, calendarList[position].dateInt)
                }
            }
            is LeafViewHolder -> {
                holder.initView(position)
            }
            is EmotionViewHolder -> {
                holder.initView(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 42
    }

    fun setDate(date : Date){
        this.date = date
    }

    fun setViewMode(viewMode : Int){
        return if (viewMode == 0) this.ViewMode = DONELIST_MODE else this.ViewMode = EMOTION_MODE
    }
}