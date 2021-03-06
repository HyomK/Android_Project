package com.likefirst.btos.ui.view.posting


import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemDiaryEmotionRvBinding

class DiaryEmotionRVAdapter(val emotionColorIds : ArrayList<Int>,
                            val emotionGrayIds : ArrayList<Int>,
                            val emotionNames : Array<String>, val emotionSelectedIdx : Int?,
                            val context : Context, val fontIdx : Int
) : RecyclerView.Adapter<DiaryEmotionRVAdapter.ViewHolder>(){

    var defaultSelectedIdx : Int? = emotionSelectedIdx
    var itemList = emotionColorIds

    inner class ViewHolder(val binding:ItemDiaryEmotionRvBinding) : RecyclerView.ViewHolder(binding.root) {
        // 아무것도 선택하지 않았을 때
        fun initView(emotionColorId : Int){
            binding.itemDiaryEmotionIv.setImageResource(emotionColorId)
            binding.itemDiaryEmotionTv.visibility = View.INVISIBLE
        }
        // 하나만 이미 선택되어 있는 상태
        fun initSelectedView(emotionColorId : Int, emotionGrayId: Int, position: Int, emotionSelectedIdx: Int, emotionName : String){
            val fontList = context.resources.getStringArray(R.array.fontEng)
            val font = context.resources.getIdentifier(fontList[fontIdx], "font", context.packageName)
            binding.itemDiaryEmotionTv.typeface = ResourcesCompat.getFont(context,font) // 폰트지정
            if (position == emotionSelectedIdx){
                binding.itemDiaryEmotionIv.setImageResource(emotionColorId)
                binding.itemDiaryEmotionIv.visibility = View.VISIBLE
                binding.itemDiaryEmotionTv.visibility = View.VISIBLE
                binding.itemDiaryEmotionTv.text = emotionName
            } else {
                binding.itemDiaryEmotionIv.setImageResource(emotionGrayId)
                binding.itemDiaryEmotionTv.visibility = View.INVISIBLE
            }
        }
        // 하나를 새로 선택했을 때
        fun setEmotionGray(emotionGrayId : Int){
            binding.itemDiaryEmotionIv.setImageResource(emotionGrayId)
            binding.itemDiaryEmotionTv.visibility = View.INVISIBLE
        }
        fun setEmotionSelected(emotionColorId: Int, emotionNames : String){
            val fontList = context.resources.getStringArray(R.array.fontEng)
            val font = context.resources.getIdentifier(fontList[fontIdx], "font", context.packageName)
            binding.itemDiaryEmotionTv.typeface = ResourcesCompat.getFont(context,font) // 폰트지정
            binding.itemDiaryEmotionIv.setImageResource(emotionColorId)
            binding.itemDiaryEmotionTv.visibility = View.VISIBLE
            binding.itemDiaryEmotionTv.text = emotionNames
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryEmotionRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val position = getItemViewType(pos)
        Log.d("onBindViewHolder", "bind!!!")
        if(defaultSelectedIdx != null){
            holder.initSelectedView(emotionColorIds[position], emotionGrayIds[position], position, defaultSelectedIdx!!, emotionNames[position])
            Log.d("initSelectedView", position.toString())
        } else {
            holder.initView(itemList[position])
            Log.d("initView", position.toString())
        }
        holder.binding.itemDiaryEmotionIv.setOnClickListener {
            DiaryActivity.emotionIdx = position
            defaultSelectedIdx = null
            itemList = emotionGrayIds
            notifyItemRangeChanged(0, itemCount, "setEmotionGray")
            Log.d("notify_1", "notify_1")
            notifyItemChanged(position, "setEmotion")
            Log.d("notify_2", "notify_2")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
            Log.d("payLoadEmpty","empty")
        } else {
            for (payload in payloads){
                if(payload is String){
                    val type = payload.toString()
                    if(TextUtils.equals(type, "setEmotionGray")){
                        holder.setEmotionGray(emotionGrayIds[position])
                        Log.d("setEmotionGray", position.toString())
                    }
                    if (TextUtils.equals(type, "setEmotion")){
                        holder.setEmotionSelected(emotionColorIds[position], emotionNames[position])
                        Log.d("setEmotion", position.toString())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return emotionColorIds.size
    }

    override fun getItemViewType( position : Int) : Int{
        return position
    }
}