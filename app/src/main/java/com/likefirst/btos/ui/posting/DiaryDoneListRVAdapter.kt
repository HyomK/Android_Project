package com.likefirst.btos.ui.posting

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.ItemDiaryDoneListRvBinding

class DiaryDoneListRVAdapter: RecyclerView.Adapter<DiaryDoneListRVAdapter.ViewHolder>() {
    val doneLists : ArrayList<String> = ArrayList()
    var doneListWatcher = ""

    interface ItemClickListener

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
        holder.binding.itemDiaryDoneListDeleteIv.setOnClickListener {
            deleteDoneList(position)
        }
        holder.itemView.setOnClickListener{
            val text = holder.binding.itemDiaryDoneListTv.text.toString()
            holder.binding.itemDiaryDoneListTv.visibility = View.INVISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.setText(text)

            //엔터 눌렀을 때 업데이트
            holder.binding.itemDiaryDoneListEt.setOnKeyListener { p0, keyCode, event ->
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                    if(holder.binding.itemDiaryDoneListEt.text.length < 100){
                        holder.binding.itemDiaryDoneListEt.text.delete( holder.binding.itemDiaryDoneListEt.selectionStart - 1, holder.binding.itemDiaryDoneListEt.selectionStart)
                    }
                    setDoneList(holder, position)
                }
                false
            }

            // 포커스 벗어났을 때 doneList 업데이트
            holder.binding.itemDiaryDoneListEt.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus){
                        setDoneList(holder, position)
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return doneLists.size
    }

    fun setDoneList(holder : ViewHolder, position: Int){
        if(holder.binding.itemDiaryDoneListEt.text.toString() == ""){
            deleteDoneList(position)
            holder.binding.itemDiaryDoneListTv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.GONE
        } else {
            updateDoneList(position, holder.binding.itemDiaryDoneListEt.text.toString())
            holder.binding.itemDiaryDoneListTv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.GONE
        }
    }

    fun updateDoneList(position : Int, doneList : String){
        doneLists[position] = doneList
        notifyItemChanged(position)
        DiaryActivity.doneLists = doneLists
    }

    fun deleteDoneList(position : Int){
        doneLists.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        DiaryActivity.doneLists = doneLists
    }

    fun addDoneList(text : String){
        this.doneLists.add(text)
        notifyItemInserted(doneLists.size)
        DiaryActivity.doneLists = doneLists
    }
}