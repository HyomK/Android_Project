package com.likefirst.btos.ui.splash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.likefirst.btos.R

class TutorialViewPagerAdapter(private val context: Context, private val items: ArrayList<Int>) :
    RecyclerView.Adapter<TutorialViewPagerAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v:View, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tutorial, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageUrl.clipToOutline = true
        Glide.with(context).load(items[position]).into(holder.imageUrl)
        if(position==2){
            holder.finishButton.visibility = View.VISIBLE
        }else{
            holder.finishButton.visibility = View.GONE
        }
        holder.finishButton.setOnClickListener {
            listener?.onItemClick(it,position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageUrl: ImageView = view.findViewById(R.id.tutorial_iv)
        val finishButton : TextView = view.findViewById(R.id.tutorial_okay_tv)
    }
}
