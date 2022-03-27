package com.likefirst.btos.ui.view.history

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.HistoryList
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.databinding.ItemHistoryBinding
import com.likefirst.btos.databinding.ItemHistoryLoadingBinding


class HistoryBasicRecyclerViewAdapter(private val context: Context?, private val userIdx : Int, val filtering: LiveData<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_TYPE_ITEM :Int= 0
    private val VIEW_TYPE_LOADING:Int = 1

    private val SENDER_VIEW_TYPE_ITEM :Int= 2
    private val SENDER_VIEW_TYPE_LOADING:Int = 3

    var _senderItems = ArrayList<SenderList?>()
    var dlItems = ArrayList<Content?>()
    var filter = filtering.value
    val fonts= context!!.resources.getStringArray(R.array.fontEng)
  /*  val data = DataSet()*/


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
        viewType: Int,
    ): RecyclerView.ViewHolder {

        if (viewType === VIEW_TYPE_ITEM){
            val view = LayoutInflater.from(parent.context)
                .inflate(com.likefirst.btos.R.layout.item_history, parent, false)
            val binding : ItemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return HistoryViewHolder(binding)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(com.likefirst.btos.R.layout.item_history_loading,parent, false)
            val binding =ItemHistoryLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, pos: Int) {
        if(holder is HistoryViewHolder){
            var position =pos
            if(_senderItems.isNotEmpty()){
                Log.e("bindingHolder-sender","${position}/${pos}")
                if(_senderItems[position]!=null){
                    holder.senderBind(_senderItems[position]!!)
                    holder.binding.itemHistoryLayout.setOnClickListener {
                        _senderItems[position]!!.firstContent.senderNickName?.let { it1 ->
                            mItemClickListener.moveToSenderDetail(it1)
                        }
                    }
                }

            }
            if(dlItems.isNotEmpty()){
                Log.e("bindingHolder-dl","${position}/${pos}")
                var position =pos
                if(dlItems[position]!=null){
                    dlItems[position].let { holder.dlBind(it!!) }
                    holder.binding.itemHistoryLayout.setOnClickListener {
                        mItemClickListener.moveToHistoryList(userIdx,dlItems[position]!!.type,  dlItems[position]!!.typeIdx)
                    }
                }
            }

        }else{
            //progress bar
        }

    }


    override fun getItemViewType( position : Int) : Int{
        if("sender"== filtering.value){
            if(_senderItems.get(position) == null)
                return VIEW_TYPE_LOADING
            else return VIEW_TYPE_ITEM
        }else{
            if(dlItems.get(position) == null)
                return VIEW_TYPE_LOADING
            else return VIEW_TYPE_ITEM
        }
    }


    override fun getItemCount(): Int {
        if("sender"== filtering.value){
            return   _senderItems.size
        }else{
            return dlItems.size
        }
    }

    inner class LoadingViewHolder(private val binding: ItemHistoryLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class HistoryViewHolder(val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun senderBind(item : SenderList){

            val fontId= context!!.resources.getIdentifier(fonts[item.firstContent.senderFontIdx!!],"font", context!!.packageName)
            binding.itemHistoryContent.typeface=ResourcesCompat.getFont(context,fontId)
            binding.itemHistoryDate.typeface=ResourcesCompat.getFont(context,fontId)
            binding.itemHistorySender.typeface=ResourcesCompat.getFont(context,fontId)

            if(item.firstContent.type == "diary"){
                binding.itemHistoryBg.setBackgroundResource(R.drawable.diary_repeat_bg)
                if(item.firstContent.emotionIdx != "0") {
                    val emotion = context!!.resources.getIdentifier("emotion"+item.firstContent.emotionIdx, "drawable", context.packageName)
                    binding.itemHistoryEmotion.setImageResource(emotion)
                    binding.itemHistoryEmotion.visibility = View.VISIBLE
                }else{
                    binding.itemHistoryEmotion.visibility = View.GONE
                }
            }else{
                binding.itemHistoryBg.setBackgroundResource(R.drawable.history_repeat_bg)
                binding.itemHistoryEmotion.visibility = View.GONE
            }
            binding.itemHistoryArrow.visibility = View.VISIBLE
            binding.itemHistorySenderTitle.visibility = View.VISIBLE

            binding.itemHistorySenderTitle.text = "${item.firstContent.senderNickName} (${item.historyListNum})"
            binding.itemHistoryContent.text = item.firstContent.content
            binding.itemHistoryDate.text = item.firstContent.sendAt
            binding.itemHistorySender.text = item.firstContent.senderNickName

            if(item.firstContent.doneListNum != 0){
                val done = context!!.resources.getIdentifier("leaf"+item.firstContent.doneListNum, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }else{
                binding.itemHistoryDone.visibility = View.GONE
            }

        }
        fun dlBind(item : Content){
            Log.e("binding-dl",filtering.value.toString()+"/${item.emotionIdx?.toInt()}")

            //font set
            val fonts= context!!.resources.getStringArray(R.array.fontEng)
            val fontId= context!!.resources.getIdentifier(fonts[item.senderFontIdx!!],"font", context!!.packageName)
            binding.itemHistoryContent.typeface=ResourcesCompat.getFont(context,fontId)
            binding.itemHistoryDate.typeface=ResourcesCompat.getFont(context,fontId)
            binding.itemHistorySender.typeface=ResourcesCompat.getFont(context,fontId)


            if(filtering.value== "diary"||item.type=="diary"){
                binding.itemHistoryBg.setBackgroundResource(R.drawable.diary_repeat_bg)
                if(item.emotionIdx?.toInt() != 0) {
                    val emotion = context!!.resources.getIdentifier("emotion"+item.emotionIdx, "drawable", context.packageName)
                    Log.e("binding-dl","${emotion}")
                    binding.itemHistoryEmotion.setImageResource(emotion)
                    binding.itemHistoryEmotion.visibility=View.VISIBLE
                }
            }else{
                binding.itemHistoryBg.setBackgroundResource(R.drawable.history_repeat_bg)
                binding.itemHistoryEmotion.visibility = View.GONE
            }

            binding.itemHistoryArrow.visibility = View.GONE
            binding.itemHistorySenderTitle.visibility = View.GONE
            binding.itemHistoryContent.text = item.content
            binding.itemHistoryDate.text = item.sendAt
            binding.itemHistorySender.text = item.senderNickName

            if(item.doneListNum != 0){
                val done = context!!.resources.getIdentifier("leaf"+item.doneListNum, "drawable", context.packageName)
                binding.itemHistoryDone.setImageResource(done)
            }else{
                binding.itemHistoryDone.visibility = View.GONE
            }

        }
    }


   // @SuppressLint("NotifyDataSetChanged")
    fun setSenderItems(items: ArrayList<SenderList?>?){
        if(items == null) return
        _senderItems=items
        dlItems.clear()

      
    }


    fun addSenderItems(items: ArrayList<SenderList>){
        _senderItems.addAll(items)

    }


    fun setdlItems(items: ArrayList<Content?>?){
        if(items == null) return
        dlItems=items
        _senderItems.clear()

    }


    fun addlItems(items: ArrayList<Content?>?){
        if(items == null) return
        Log.e("addedItem : ", items.toString())
        dlItems.addAll(items!!)

    }


    @SuppressLint("NotifyDataSetChanged")
    fun clearSenderItems(){
        _senderItems.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun cleardlItems(){
        dlItems.clear()
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun removeSenderItem(index : Int){
        if(index < _senderItems.size){
            _senderItems.removeAt(index)
            notifyItemChanged(index)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removedlItem(index : Int){
        if(index < dlItems.size){
            dlItems.removeAt(index)
            notifyItemChanged(index)
        }
    }


    fun isSenderEmpty() : Boolean{
        return _senderItems.isEmpty()
    }

    fun isDLEmpty() : Boolean{
        return dlItems.isEmpty()
    }

    fun setFont(idx:Int){
        val fonts= context!!.resources.getStringArray(R.array.fontEng)
        val fontId= context!!.resources.getIdentifier(fonts[idx],"font", context!!.packageName)
    }

}