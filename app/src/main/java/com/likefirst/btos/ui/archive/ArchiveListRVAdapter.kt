package com.likefirst.btos.ui.archive

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.viewer.response.ArchiveListDiaryList
import com.likefirst.btos.databinding.ItemArchiveListRvDiaryBinding
import com.likefirst.btos.databinding.ItemArchiveListRvMonthBinding
import java.lang.RuntimeException

class ArchiveListRVAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DIARY_TYPE = 0
    private val MONTH_TYPE = 1
    private val LOADING_TYPE = 2
    private var diaryList = arrayListOf<Any>()

    inner class DiaryViewHolder(val binding : ItemArchiveListRvDiaryBinding): RecyclerView.ViewHolder(binding.root) {
        fun initView(diaryInfo : ArchiveListDiaryList){
            var leafImg = 0
            binding.archiveListPreviewContentsTv.text = diaryInfo.content
            binding.archiveListPreviewDateTv.text = diaryInfo.diaryDate
            // emotionImg 바인딩
            if (diaryInfo.emotionIdx == 0){
                binding.archiveListPreviewEmotionIv.visibility = View.INVISIBLE
            } else {
                val emotionRes = context.resources.getIdentifier("emotion${diaryInfo.emotionIdx}", "drawable", context.packageName)
                binding.archiveListPreviewEmotionIv.setImageResource(emotionRes)
            }
            // doneListImg 바인딩
            when (diaryInfo.doneListNum) {
                in 0..2 -> {
                    leafImg = context.resources.getIdentifier("leaf1", "drawable", context.packageName)
                }
                in 3..4 -> {
                    leafImg = context.resources.getIdentifier("leaf2", "drawable", context.packageName)
                }
                in 5..6 -> {
                    leafImg = context.resources.getIdentifier("leaf3", "drawable", context.packageName)
                }
                in 7..8 -> {
                    leafImg = context.resources.getIdentifier("leaf4", "drawable", context.packageName)
                }
                in 9..10 -> {
                    leafImg = context.resources.getIdentifier("leaf5", "drawable", context.packageName)
                }
            }
            binding.archiveListPreviewDoneListIv.setImageResource(leafImg)
        }
    }

    inner class MonthViewHolder(val binding : ItemArchiveListRvMonthBinding): RecyclerView.ViewHolder(binding.root) {
        fun initView(date: String){
            val year = date.slice(IntRange(0,3))
            val monthInt = date.slice(IntRange(5,6)).toInt() - 1
            val monthArray = context.resources.getStringArray(R.array.month)
            val month = monthArray[monthInt]
            binding.itemArchiveListYearTv.text = year
            binding.itemArchiveListMonthTv.text = month
        }
    }

    inner class LoadingViewHolder(val binding : ItemArchiveListRvMonthBinding) : RecyclerView.ViewHolder(binding.root){
        fun initView(){
            binding.itemArchiveListLoading.visibility = View.VISIBLE
            binding.itemArchiveListLoading.setAnimation("sprout_loading.json")
            binding.itemArchiveListMonthTv.visibility = View.GONE
            binding.itemArchiveListYearTv.visibility = View.GONE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            diaryList[position] is String -> {
                MONTH_TYPE
            }
            diaryList[position] is ArchiveListDiaryList -> {
                DIARY_TYPE
            }
            else -> {
                LOADING_TYPE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            DIARY_TYPE -> {
                val binding = ItemArchiveListRvDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DiaryViewHolder(binding)
            }
            MONTH_TYPE -> {
                val binding = ItemArchiveListRvMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MonthViewHolder(binding)
            }
            LOADING_TYPE -> {
                val binding = ItemArchiveListRvMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                throw RuntimeException("Unknown ViewType Error in ArchiveListRVAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is DiaryViewHolder -> {
                holder.initView(diaryList[position] as ArchiveListDiaryList)
            }
            is MonthViewHolder -> {
                holder.initView(diaryList[position] as String)
            }
            is LoadingViewHolder -> {
                holder.initView()
            }
        }
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    fun addDiaryList(dataList : ArrayList<Any>){
        diaryList.addAll(dataList)
    }

    fun deleteLoading(){
        if (diaryList[itemCount-1] == 0){ diaryList.removeAt(itemCount - 1) }
        Log.d("itemcount", itemCount.toString())
    }

    fun clearList(){
        diaryList = arrayListOf()
        notifyDataSetChanged()
    }

    fun isDiaryEmpty() : Boolean{
        return diaryList == ArrayList<String>()
    }
}