package com.likefirst.btos.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.History
import com.likefirst.btos.databinding.ItemHistoryNosenderBinding

class NoSenderRecyclerViewAdapter(private val context: Context?, private val items: List<History>)
    : RecyclerView.Adapter<NoSenderRecyclerViewAdapter.ViewHolder>(){

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
    ): NoSenderRecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryNosenderBinding = ItemHistoryNosenderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoSenderRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.itemHistoryNosenderLayout.setOnClickListener {
            mItemClickListener.MoveToDetail(items[position].historyIdx)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemHistoryNosenderBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : History){
            binding.itemHistoryNosenderContent.text = item.content
            binding.itemHistoryNosenderDate.text = item.date
            binding.itemHistoryNosenderSender.text = item.sender
            if(item.emotion!=null) {
                val emotion = context!!.resources.getIdentifier("emotion"+item.emotion, "drawable", context.packageName)
                binding.itemHistoryNosenderEmotion.setImageResource(emotion)
            }
            if(item.done!=null){
                val done = context!!.resources.getIdentifier("emotion"+item.done, "drawable", context.packageName)
                binding.itemHistoryNosenderDone.setImageResource(done)
            }
        }
    }
}