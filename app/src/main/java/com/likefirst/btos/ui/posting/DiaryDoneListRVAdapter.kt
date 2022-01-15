package com.likefirst.btos.ui.posting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemDiaryDoneListRvBinding

class DiaryDoneListRVAdapter: RecyclerView.Adapter<DiaryDoneListRVAdapter.ViewHolder>() {
    val doneLists : ArrayList<String> = ArrayList()

    interface ItemClickListener{

    }

    private lateinit var mItemClickListener : ItemClickListener

    inner class ViewHolder(val binding: ItemDiaryDoneListRvBinding): RecyclerView.ViewHolder(binding.root) {
        fun initView(doneList : String) {
            binding.itemDiaryDoneListTv.text = doneList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryDoneListRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(doneLists[position])
    }

    override fun getItemCount(): Int {
        return doneLists.size
    }

    fun addDoneList(text : String){
        this.doneLists.add(text)
        notifyDataSetChanged()
    }
}