package com.likefirst.btos.ui.view.history

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.databinding.ItemHistoryBinding

class HistoryAdapter(private val context: Context?, private val userIdx : Int, val filtering: LiveData<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    inner class DataSet(){
        var senderItems = ArrayList<SenderList>()
        var dlItems = ArrayList<Content>()
    }

    var filter = filtering.value
    val data = DataSet()


    interface MyItemClickListener{
        fun moveToSenderDetail(sender : String)
        fun moveToHistoryList(userIdx : Int, type : String, typeIdx : Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.ViewHolder {
        val binding : ItemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, pos: Int) {
        var position =getItemViewType(pos)
        if(data.senderItems.isNotEmpty()){
            holder.senderBind(data.senderItems[position])
            holder.binding.itemHistoryLayout.setOnClickListener {
                data.senderItems[position].firstContent.senderNickName?.let { it1 ->
                    mItemClickListener.moveToSenderDetail(it1)
                }
            }
        }
        if(data.dlItems.isNotEmpty()){
            data.dlItems[position].let { holder.dlBind(it) }
            holder.binding.itemHistoryLayout.setOnClickListener {
                data.dlItems[position].let { it1 ->
                    data.dlItems[position].let { it2 ->
                        mItemClickListener.moveToHistoryList(userIdx, it2.type, it1.typeIdx) } }

            }

        }
    }


    override fun getItemViewType( position : Int) : Int{
        return position
    }


    override fun getItemCount(): Int {
        if("sender"== filtering.value){
            return   data.senderItems.size
        }else{
            return data.dlItems.size
        }
    }

    inner class ViewHolder(val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun senderBind(item : SenderList){
            if(item.firstContent.type == "diary"){
                binding.itemHistoryBg.setImageResource(R.drawable.ic_bg_diary)
            }
            binding.itemHistorySenderTitle.text = "${item.firstContent.senderNickName} (${item.historyListNum})"
            binding.itemHistoryContent.text = item.firstContent.content
            binding.itemHistoryDate.text = item.firstContent.sendAt
            binding.itemHistorySender.text = item.firstContent.senderNickName
            if(item.firstContent.emotionIdx != "0") {
                val emotion = context!!.resources.getIdentifier("emotion"+item.firstContent.emotionIdx, "drawable", context.packageName)
                binding.itemHistoryEmotion.setImageResource(emotion)
            }else{
                binding.itemHistoryEmotion.visibility = View.GONE
            }
            if(item.firstContent.doneListNum != 0){
                val done = context!!.resources.getIdentifier("leaf"+item.firstContent.doneListNum, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }else{
                binding.itemHistoryDone.visibility = View.GONE
            }

        }
        fun dlBind(item : Content){

            if(filtering.value== "diary"){
                binding.itemHistoryBg.setImageResource(R.drawable.ic_bg_diary)
            }else{
                binding.itemHistoryBg.setBackgroundResource(R.drawable.history_repeat_bg)
            }
            binding.itemHistoryArrow.visibility = View.GONE
            binding.itemHistorySenderTitle.visibility = View.GONE
            binding.itemHistoryContent.text = item.content
            binding.itemHistoryDate.text = item.sendAt
            binding.itemHistorySender.text = item.senderNickName
            if(item.emotionIdx != "0") {
                val emotion = context!!.resources.getIdentifier("emotion"+item.emotionIdx, "drawable", context.packageName)
                binding.itemHistoryEmotion.setImageResource(emotion)
            }else{
                binding.itemHistoryEmotion.visibility = View.GONE
            }
            if(item.doneListNum != 0){
                val done = context!!.resources.getIdentifier("leaf"+item.doneListNum, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }else{
                binding.itemHistoryDone.visibility = View.GONE
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setSenderItems(items: ArrayList<SenderList>){
        data.senderItems=items
        data.dlItems.clear()
        notifyDataSetChanged()
        Log.e("history-senderlist",data.senderItems.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdlItems(items: ArrayList<Content>){
        this.data.dlItems = items
        this.data.senderItems.clear()
        notifyDataSetChanged()
        Log.e("history-list",data.dlItems.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSenderItems(){
        this.data.senderItems.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun cleardlItems(){
        this.data.dlItems.clear()
        notifyDataSetChanged()
    }

    fun isSenderEmpty() : Boolean{
        return data.senderItems.isEmpty()
    }

    fun isDLEmpty() : Boolean{
        return data.dlItems.isEmpty()
    }

}