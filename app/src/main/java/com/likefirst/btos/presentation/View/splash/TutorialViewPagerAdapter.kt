package com.likefirst.btos.presentation.View.splash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.likefirst.btos.R


class TutorialViewPagerAdapter(private val context: Context, private val items: Array<String>, private val intro: Array<String>) :
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

        holder.lottieUrl.setAnimation(items[position])
        holder.lottieUrl.playAnimation()
        holder.introtext.setText(intro[position])
        if(position==items.size-1){
            holder.finishButton.visibility = View.VISIBLE
        }else{
            holder.finishButton.visibility = View.GONE
        }
        holder.finishButton.setOnClickListener {
            listener?.onItemClick(it,position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val lottieUrl: LottieAnimationView = view.findViewById(R.id.tutorial_lottie)
        val finishButton : TextView = view.findViewById(R.id.tutorial_okay_tv)
        val introtext : TextView = view.findViewById(R.id.tutorial_intro_tv)
    }
}
