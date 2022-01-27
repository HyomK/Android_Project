package com.likefirst.btos.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.History
import com.likefirst.btos.databinding.ItemHistoryBinding

class SenderRecyclerViewAdapter(private val context: Context?, private val items: List<History>)
    : RecyclerView.Adapter<SenderRecyclerViewAdapter.ViewHolder>(){

    interface MyItemClickListener{
        fun MoveToSenderDetail(historyIdx : Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SenderRecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SenderRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.itemHistoryLayout.setOnClickListener {
            mItemClickListener.MoveToSenderDetail(items[position].historyIdx)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : History){
            binding.itemHistorySenderTitle.text = item.sender
            binding.itemHistoryContent.text = item.content
            binding.itemHistoryDate.text = item.date
            binding.itemHistorySender.text = item.sender
            if(item.emotion!=null) {
                val emotion = context!!.resources.getIdentifier("emotion"+item.emotion, "drawable", context.packageName)
                binding.itemHistoryEmotion.setImageResource(emotion)
            }
            if(item.done!=null){
                val done = context!!.resources.getIdentifier("emotion"+item.done, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }
        }
    }
}