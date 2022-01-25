package com.likefirst.btos.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemArchiveListRvDiaryBinding
import com.likefirst.btos.databinding.ItemArchiveListRvMonthBinding
import java.lang.RuntimeException

class ArchiveListRVAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DIARY_TYPE = 0
    private val MONTH_TYPE = 1

    inner class DiaryViewHolder(val binding : ItemArchiveListRvDiaryBinding): RecyclerView.ViewHolder(binding.root) {

    }

    inner class MonthViewHolder(val binding : ItemArchiveListRvMonthBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
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

    }

    override fun getItemCount(): Int {
        return 0
    }
}