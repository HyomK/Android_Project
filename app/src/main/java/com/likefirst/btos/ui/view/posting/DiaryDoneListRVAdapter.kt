package com.likefirst.btos.ui.view.posting

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemDiaryDoneListRvBinding

class DiaryDoneListRVAdapter(private val start : String,
                             val context : Context, val fontIdx : Int): RecyclerView.Adapter<DiaryDoneListRVAdapter.ViewHolder>() {
    val doneLists : ArrayList<String> = ArrayList()
    var doneListWatcher = ""

    interface ItemClickListener {
        fun onDoneListEnter(view : View)
    }

    fun setOnDoneListEnter(itemClickListener: ItemClickListener){
        mItemClickListener = itemClickListener
    }

    private lateinit var mItemClickListener : ItemClickListener

    inner class ViewHolder(val binding: ItemDiaryDoneListRvBinding): RecyclerView.ViewHolder(binding.root) {
        fun initView(doneList : String) {
            binding.itemDiaryDoneListTv.text = doneList
            val fontList = context.resources.getStringArray(R.array.fontEng)
            val font = context.resources.getIdentifier(fontList[fontIdx], "font", context.packageName)
            binding.itemDiaryDoneListTv.typeface = ResourcesCompat.getFont(context,font)
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
        if(start == "history"){
            holder.binding.itemDiaryDoneListDeleteIv.visibility = View.GONE
            holder.binding.itemDiaryDoneListTv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.GONE
        }
        holder.itemView.setOnClickListener{
            if(start == "diary"){
                val text = holder.binding.itemDiaryDoneListTv.text.toString()
                val fontList = context.resources.getStringArray(R.array.fontEng)
                val font = context.resources.getIdentifier(fontList[fontIdx], "font", context.packageName)

                holder.binding.itemDiaryDoneListDeleteIv.visibility = View.GONE
                holder.binding.itemDiaryDoneListTv.visibility = View.GONE
                holder.binding.itemDiaryDoneListEt.apply{
                    visibility = View.VISIBLE
                    setText(text)
                    requestFocus()
                    typeface = ResourcesCompat.getFont(context,font)
                    setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                                val scrollView = findViewById<NestedScrollView>(R.id.diary_sv)
                            scrollView.postDelayed(Runnable {
                                scrollView.smoothScrollBy(0,800)
                            }, 100)
                        }
                    }
                }
//                    holder.binding.itemDiaryDoneListEt.
//                    holder.binding.itemDiaryDoneListEt.
//                    holder.binding.itemDiaryDoneListEt.typeface = ResourcesCompat.getFont(context,font)

                    //엔터 눌렀을 때 업데이트
                    holder.binding.itemDiaryDoneListEt.setOnKeyListener { p0, keyCode, event ->
                        if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                            if(holder.binding.itemDiaryDoneListEt.text.length < 100){
                                holder.binding.itemDiaryDoneListEt.text.delete( holder.binding.itemDiaryDoneListEt.selectionStart - 1, holder.binding.itemDiaryDoneListEt.selectionStart)
                            }
                            setDoneList(holder, position)
                            mItemClickListener.onDoneListEnter(it)
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
        }

    override fun getItemCount(): Int {
        return doneLists.size
    }

    fun setDoneList(holder : ViewHolder, position: Int){
        if(holder.binding.itemDiaryDoneListEt.text.toString() == ""){
            deleteDoneList(position)
            holder.binding.itemDiaryDoneListDeleteIv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListTv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.GONE
        } else {
            updateDoneList(position, holder.binding.itemDiaryDoneListEt.text.toString())
            holder.binding.itemDiaryDoneListDeleteIv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListTv.visibility = View.VISIBLE
            holder.binding.itemDiaryDoneListEt.visibility = View.GONE
        }
    }

    fun updateDoneList(position : Int, doneList : String){
        doneLists[position] = doneList
        notifyItemChanged(position)
        notifyItemRangeChanged(position, itemCount)
        DiaryActivity.doneLists = doneLists
    }

    fun deleteDoneList(position : Int){
        doneLists.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position-1, itemCount)
        DiaryActivity.doneLists = doneLists
    }

    fun addDoneList(text : String){
        this.doneLists.add(text)
        notifyItemInserted(doneLists.size)
        DiaryActivity.doneLists = doneLists
    }
}