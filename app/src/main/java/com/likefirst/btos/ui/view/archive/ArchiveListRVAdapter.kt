package com.likefirst.btos.ui.view.archive

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.viewer.response.ArchiveListDiaryList
import com.likefirst.btos.databinding.ItemArchiveListRvDiaryBinding
import com.likefirst.btos.databinding.ItemArchiveListRvMonthBinding
import java.lang.RuntimeException

class ArchiveListRVAdapter(val context : Context, val fontIdx : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DIARY_TYPE = 0
    private val MONTH_TYPE = 1
    private var diaryList = arrayListOf<Any>()

    inner class DiaryViewHolder(val binding : ItemArchiveListRvDiaryBinding): RecyclerView.ViewHolder(binding.root) {
        fun initView(diaryInfo : ArchiveListDiaryList){
            var leafImg = 0
            binding.archiveListPreviewContentsTv.text = diaryInfo.content
            binding.archiveListPreviewDateTv.text = diaryInfo.diaryDate
            val fontList = context.resources.getStringArray(R.array.fontEng)
            val font = context.resources.getIdentifier(fontList[fontIdx], "font", context.packageName)
            binding.archiveListPreviewContentsTv.typeface = ResourcesCompat.getFont(context, font)
            binding.archiveListPreviewDateTv.typeface = ResourcesCompat.getFont(context, font)

            // emotionImg 바인딩
            val emotionRes = context.resources.getIdentifier("emotion${diaryInfo.emotionIdx}", "drawable", context.packageName)
            binding.archiveListPreviewEmotionIv.setImageResource(emotionRes)

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

    override fun getItemViewType(position: Int): Int {
        return when {
            diaryList[position] is String -> {
                MONTH_TYPE
            }
            else -> {
                DIARY_TYPE
            }
        }
    }

    interface DiarySelectedListener {
        fun onDiarySelect(diaryIdx : Int, position: Int)
    }

    lateinit var mDiarySelectedListener : DiarySelectedListener

    fun setDiarySelectedListener(diarySelectedListener: DiarySelectedListener){
        this.mDiarySelectedListener = diarySelectedListener
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
            else -> {
                throw RuntimeException("Unknown ViewType Error in ArchiveListRVAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is DiaryViewHolder -> {
                holder.initView(diaryList[position] as ArchiveListDiaryList)
                holder.itemView.setOnClickListener {
                    mDiarySelectedListener.onDiarySelect((diaryList[position] as ArchiveListDiaryList).diaryIdx, position)
                }
            }
            is MonthViewHolder -> {
                holder.initView(diaryList[position] as String)
            }
        }
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    fun addDiaryList(dataList : ArrayList<Any>){
        diaryList.addAll(dataList)
    }

    fun clearList(){
        diaryList = arrayListOf()
        notifyDataSetChanged()
    }

    fun isDiaryEmpty() : Boolean{
        return diaryList == ArrayList<String>()
    }

    fun updateList(position : Int, doneListNum : Int, emotionIdx : Int, content : String) {
        (diaryList[position] as ArchiveListDiaryList).doneListNum = doneListNum
        (diaryList[position] as ArchiveListDiaryList).emotionIdx = emotionIdx
        (diaryList[position] as ArchiveListDiaryList).content = content
        notifyItemChanged(position)
    }
}