package com.likefirst.btos.ui.posting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemDiaryDoneListRvBinding


class DiaryViewerDoneListRVAdapter(val doneLists : ArrayList<String>) : RecyclerView.Adapter<DiaryViewerDoneListRVAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemDiaryDoneListRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initItem(position: Int){
            binding.itemDiaryDoneListDeleteIv.visibility = View.GONE
            binding.itemDiaryDoneListTv.text = doneLists[position]

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryDoneListRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initItem(position)
    }

    override fun getItemCount(): Int {
        return doneLists.size
    }
}