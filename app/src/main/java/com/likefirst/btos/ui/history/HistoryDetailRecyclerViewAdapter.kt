package com.likefirst.btos.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.HistoryDetail
import com.likefirst.btos.databinding.ItemHistoryDetailBinding

class HistoryDetailRecyclerViewAdapter(private val context: Context?, private val items: List<HistoryDetail>)
    : RecyclerView.Adapter<HistoryDetailRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryDetailRecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryDetailBinding = ItemHistoryDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryDetailRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemHistoryDetailBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : HistoryDetail){
            binding.itemHistoryDetailContent.text = item.content
            binding.itemHistoryDetailDate.text = item.date
            binding.itemHistoryDetailSender.text = item.sender
        }
    }
}