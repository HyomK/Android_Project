package com.likefirst.btos.ui.posting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemDiaryEmotionRvBinding

class DiaryEmotionRVAdapter : RecyclerView.Adapter<DiaryEmotionRVAdapter.ViewHolder>(){
    inner class ViewHolder(val binding:ItemDiaryEmotionRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(){
            binding.itemDiaryEmotionTv.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryEmotionRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView()
    }

    override fun getItemCount(): Int {
        return 8
    }
}