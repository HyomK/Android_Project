package com.likefirst.btos.ui.posting

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemDiaryEmotionRvBinding

class DiaryEmotionRVAdapter(val emotionColorIds : ArrayList<Int>,
                            val emotionGrayIds : ArrayList<Int>,
                            val emotionNames : Array<String>) : RecyclerView.Adapter<DiaryEmotionRVAdapter.ViewHolder>(){

    inner class ViewHolder(val binding:ItemDiaryEmotionRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(emotionColorId : Int){
            binding.itemDiaryEmotionIv.setImageResource(emotionColorId)
            binding.itemDiaryEmotionTv.visibility = View.INVISIBLE
        }
        fun setEmotionGray(emotionGrayId : Int){
            binding.itemDiaryEmotionIv.setImageResource(emotionGrayId)
            binding.itemDiaryEmotionTv.visibility = View.INVISIBLE
        }
        fun setEmotionSelected(emotionColorId: Int, emotionNames : String){
            binding.itemDiaryEmotionIv.setImageResource(emotionColorId)
            binding.itemDiaryEmotionTv.visibility = View.VISIBLE
            binding.itemDiaryEmotionTv.text = emotionNames
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryEmotionRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(emotionColorIds[position])
        holder.binding.itemDiaryEmotionIv.setOnClickListener {
            notifyItemRangeChanged(0, itemCount, "setEmotionGray")
            notifyItemChanged(position, "setEmotion")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads){
                if(payload is String){
                    val type = payload.toString()
                    if(TextUtils.equals(type, "setEmotionGray")){
                        holder.setEmotionGray(emotionGrayIds[position])
                    }
                    if (TextUtils.equals(type, "setEmotion")){
                        holder.setEmotionSelected(emotionColorIds[position], emotionNames[position])
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return emotionColorIds.size
    }

}