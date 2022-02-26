package com.likefirst.btos.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.HistoryList
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.databinding.ItemHistoryDetailDiaryBinding
import com.likefirst.btos.ui.posting.DiaryDoneListRVAdapter

class HistoryDetailRecyclerViewAdapter(
    private val context: Context,
    private val emotionName: Array<String>,
    private val historyDetailRv: RecyclerView
)
    : RecyclerView.Adapter<HistoryDetailRecyclerViewAdapter.ViewHolder>(){

    val historyItems = ArrayList<HistoryList>()
    var positioning : Int = -1

    interface MyItemClickListener{
        fun foundPositioning(position : Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryDetailRecyclerViewAdapter.ViewHolder {
        val binding : ItemHistoryDetailDiaryBinding = ItemHistoryDetailDiaryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryDetailRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    inner class ViewHolder(val binding : ItemHistoryDetailDiaryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : HistoryList){
            val userDB = UserDatabase.getInstance(context)!!.userDao()
            if(item.type == "letter" || item.type == "reply"){
                binding.itemHistoryDetailBgIv.setImageResource(0)
                binding.itemHistoryDetailBgIv.setBackgroundResource(R.drawable.history_repeat_bg)
            }
            binding.diaryViewerContentsTv.text = item.content
            binding.diaryViewerDateTv.text = item.sendAt
            binding.diaryViewerNameTv.text = item.senderNickName
            if(item.emotionIdx != "0") {
                val emotion = context!!.resources.getIdentifier("emotion"+item.emotionIdx, "drawable", context.packageName)
                binding.diaryViewerEmotionIv.setImageResource(emotion)
                binding.diaryViewerEmotionTv.text = emotionName[item.emotionIdx.toString().toInt()-1]
            }else{
                binding.diaryViewerEmotionIv.visibility = View.GONE
            }
            if(item.doneList != null){
                val doneListAdapter = DiaryDoneListRVAdapter("history", context, userDB.getFontIdx()!!)

                binding.diaryViewerDoneListRv.apply{
                    adapter = doneListAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                    itemAnimator = null
                }
                item.doneList.forEach {
                    doneListAdapter.addDoneList(it.content!!)
                }
            }else{
                binding.diaryViewerDoneListRv.visibility = View.GONE
            }
        }
    }

    fun setHistoryItems(items: ArrayList<HistoryList>){
        this.historyItems.clear()
        this.historyItems.addAll(items)
    }

    fun getPosition() : Int = positioning


}