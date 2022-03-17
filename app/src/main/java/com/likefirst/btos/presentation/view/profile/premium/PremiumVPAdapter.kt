package com.likefirst.btos.presentation.view.profile.premium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R

class PremiumVPAdapter (var items: ArrayList<Int> = arrayListOf()) : RecyclerView.Adapter<PremiumVPAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Image : ImageView = view.findViewById(R.id.item_premium_iv)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       val view= LayoutInflater.from(parent.context)
           .inflate(R.layout.item_premium_vp, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Image.setImageResource(items[position])
    }

    override fun getItemCount(): Int = items.size
}