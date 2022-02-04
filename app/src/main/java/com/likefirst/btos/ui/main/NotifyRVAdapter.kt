package com.likefirst.btos.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse

class NotifyRVAdapter(private val dataSet: ArrayList<NoticeDetailResponse>) : RecyclerView.Adapter< NotifyRVAdapter.ViewHolder>() {

    interface NotifyItemClickListener{
        fun onClickItem()
    }
    private lateinit var mItemClickLister:NotifyRVAdapter.NotifyItemClickListener

    fun setMyItemCLickLister(itemClickLister:NotifyItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.item_notify_tv)
        val layout : LinearLayout = view.findViewById(R.id.item_notify_layout)

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):  NotifyRVAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notify_rv, viewGroup, false)
        return NotifyRVAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: NotifyRVAdapter.ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.layout.setOnClickListener { mItemClickLister.onClickItem() }
        viewHolder.textView.text=dataSet[position].title
    }


    override fun getItemCount()= dataSet.size
}