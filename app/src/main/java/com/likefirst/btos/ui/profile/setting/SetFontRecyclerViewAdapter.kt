package com.likefirst.btos.ui.profile.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemSetFontBinding

class SetFontRecyclerViewAdapter(private val context: Context?, private val items: Array<String>)
    : RecyclerView.Adapter<SetFontRecyclerViewAdapter.ViewHolder>(){

    interface MyItemClickListener{
        fun updateFont(fontIdx : Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SetFontRecyclerViewAdapter.ViewHolder {
        val binding : ItemSetFontBinding = ItemSetFontBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetFontRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.itemSetFontTv.setOnClickListener {
            mItemClickListener.updateFont(position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemSetFontBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String){
            binding.itemSetFontTv.text = item
        }
    }
}