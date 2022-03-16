package com.likefirst.btos.presentation.View.profile.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ItemSetFontBinding

class SetFontRecyclerViewAdapter(private val context: Context?, private val items: Array<String>)
    : RecyclerView.Adapter<SetFontRecyclerViewAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun updateFont(fontIdx : Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SetFontRecyclerViewAdapter.ViewHolder {
        val binding : ItemSetFontBinding = ItemSetFontBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetFontRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position], position)

        holder.binding.itemSetFontTv.setOnClickListener {
            mItemClickListener.updateFont(position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding : ItemSetFontBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String, pos : Int){
            binding.itemSetFontTv.text = item
            setFont(pos, binding)
        }
    }

    fun setFont(pos:Int , binding: ItemSetFontBinding){
        val fonts= context?.resources?.getStringArray(R.array.fontEng)!!
        val fontId= context?.resources?.getIdentifier(fonts[pos],"font", context.packageName)!!
        binding.itemSetFontTv.typeface =ResourcesCompat.getFont(context,fontId)
    }
}