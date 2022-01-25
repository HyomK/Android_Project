package com.likefirst.btos.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.History
import com.likefirst.btos.databinding.ItemHistoryBinding

class RecyclerViewAdapter(private val context: Context?, private val items: List<History>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    interface MyItemClickListener{
        fun MoveToDetail(historyIdx : Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.itemHistoryLayout.setOnClickListener {
            mItemClickListener.MoveToDetail(items[position].historyIdx)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : History){
            binding.itemHistoryContent.text = item.content
            binding.itemHistoryDate.text = item.date
            binding.itemHistorySender.text = item.sender
            if(item.emotion!=null) {
                val emotion = context!!.resources.getIdentifier("emotion"+item.emotion, "drawable", context.packageName)
                binding.itemHistoryEmotion.setImageResource(emotion)
            }
            if(item.done!=null){
                val done = context!!.resources.getIdentifier("emotion"+item.emotion, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }
        }
    }
}