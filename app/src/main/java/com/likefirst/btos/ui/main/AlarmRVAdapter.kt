package com.likefirst.btos.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.firebase.NotificationDTO
import com.likefirst.btos.data.remote.notify.response.Alarm

class AlarmRVAdapter(private val dataSet: ArrayList<Alarm>) : RecyclerView.Adapter<AlarmRVAdapter.ViewHolder>() {

    interface AlarmItemClickListener{
        fun onClickItem(alarm: Alarm, position : Int)
    }
    private lateinit var mItemClickLister:AlarmRVAdapter.AlarmItemClickListener

    fun setMyItemCLickLister(itemClickLister:AlarmItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.item_notify_tv)
        val layout : LinearLayout = view.findViewById(R.id.item_notify_layout)

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):AlarmRVAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notify_rv, viewGroup, false)
        return AlarmRVAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: AlarmRVAdapter.ViewHolder, position: Int) {

        viewHolder.layout.setOnClickListener { mItemClickLister.onClickItem(dataSet[position],position) }
        viewHolder.textView.text=dataSet[position].content
    }

    fun remove(pos: Int){
        if(pos>=0){
            dataSet.removeAt(pos)
            notifyDataSetChanged()
        }
    }


    override fun getItemCount()= dataSet.size
}
