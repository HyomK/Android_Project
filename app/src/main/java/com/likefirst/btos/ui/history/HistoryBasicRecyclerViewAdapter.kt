package com.likefirst.btos.ui.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.databinding.ItemHistoryBinding

class HistoryBasicRecyclerViewAdapter(private val context: Context?, private val filtering : String, private val userIdx : Int)
    : RecyclerView.Adapter<HistoryBasicRecyclerViewAdapter.ViewHolder>(){

    val senderItems = ArrayList<SenderList>()
    val dlItems = ArrayList<Content>()

    interface MyItemClickListener{
        fun moveToSenderDetail(userIdx : Int, sender : String)
        fun moveToHistoryList(userIdx : Int, type : String, typeIdx : Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryBasicRecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryBasicRecyclerViewAdapter.ViewHolder, position: Int) {

        if(senderItems.isNotEmpty()){
            holder.senderBind(senderItems[position])
            holder.binding.itemHistoryLayout.setOnClickListener {
                senderItems[position].firstContent.senderNickName?.let { it1 ->
                    mItemClickListener.moveToSenderDetail(userIdx,
                        it1)
                }
            }
        }
        if(dlItems.isNotEmpty()){
            dlItems[position].let { holder.dlBind(it) }
            holder.binding.itemHistoryLayout.setOnClickListener {
                dlItems[position].let { it1 ->
                    dlItems[position].let { it2 ->
                        mItemClickListener.moveToHistoryList(userIdx, it2.type, it1.typeIdx) } }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(filtering == "sender") senderItems.size else dlItems.size
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
            if(item.type == "diary"){
                binding.itemHistoryBg.setImageResource(R.drawable.ic_bg_diary)
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

    fun setSenderItems(items: ArrayList<SenderList>){
//        this.senderItems.clear()
        this.senderItems.addAll(items)
    }

    fun setdlItems(items: ArrayList<Content>){
//        this.dlItems.clear()
        this.dlItems.addAll(items)
    }

    fun clearSenderItems(){
        this.senderItems.clear()
    }

    fun cleardlItems(){
        this.dlItems.clear()
    }

    fun isSenderEmpty() : Boolean{
        return senderItems.isEmpty()
    }

    fun isDLEmpty() : Boolean{
        return dlItems.isEmpty()
    }

}