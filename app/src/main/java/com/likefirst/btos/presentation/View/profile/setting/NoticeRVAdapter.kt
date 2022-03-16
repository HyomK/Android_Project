package com.likefirst.btos.presentation.View.profile.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import kotlin.collections.ArrayList

class NoticeRVAdapter(var items:  ArrayList<Pair<NoticeDetailResponse, Boolean>>): RecyclerView.Adapter<NoticeRVAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_notice_title)
        val date : TextView = view.findViewById(R.id.item_notice_date)
        val body : TextView = view.findViewById(R.id.item_notice_body)
        val arrow: ImageButton = view.findViewById(R.id.item_notice_arrow)
        val layoutExpand : LinearLayout = view.findViewById(R.id.item_notice_layout_expand)
        val layout : ConstraintLayout= view.findViewById(R.id.item_notice_layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val pos = getItemViewType(position)
        holder.title.text=items[pos].first.title
        holder.body.text=items[pos].first.content
        val date= items[pos].first.createdAt.split(".")
        holder.date.text="${date[0]}.${date[1]}.${date[2]}"

        holder.layout.setOnClickListener{
            val show = toggleLayout(!items[pos].second, holder.arrow,holder.layoutExpand)
            items[pos]=Pair(items[pos].first, show)
            notifyItemChanged(0,itemCount)
        }
        holder.arrow.setOnClickListener{
            val show = toggleLayout(!items[pos].second, holder.arrow,holder.layoutExpand)
            items[pos]=Pair(items[pos].first, show)
            notifyItemChanged(0,itemCount)
        }
    }


    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
        ToggleAnimation.toggleArrow(view, isExpanded)
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType( position : Int) : Int{
        return position
    }

}