package com.likefirst.btos.ui.profile.setting

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.stringToDate
import java.util.*
import kotlin.collections.ArrayList

class NoticeRVAdapter(val items:  ArrayList<Pair<String,String>> ): RecyclerView.Adapter<NoticeRVAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val body: TextView = view.findViewById(R.id.item_notice_body)
        val date : TextView = view.findViewById(R.id.item_notice_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.body.text=items[position].first
        val date= items[position].second.split(".")
        holder.date.text="${date[0]}.${date[1]}.${date[2]}"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}