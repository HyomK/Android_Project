package com.likefirst.btos.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemArchiveCalendarRvDateBinding
import com.likefirst.btos.databinding.ItemArchiveCalendarRvEmptyBinding
import java.lang.RuntimeException

class ArchiveCalendarRVAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EMPTY_CELL = 0
    private val DATE_CELL = 1

    inner class DateViewHolder(val binding : ItemArchiveCalendarRvDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(){

        }
    }

    inner class EmptyViewHolder(val binding : ItemArchiveCalendarRvEmptyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(){

        }
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
            holder.initView()
        }
    }

    override fun getItemCount(): Int {
        return 42
    }
}