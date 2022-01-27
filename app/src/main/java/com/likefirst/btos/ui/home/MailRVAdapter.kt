package com.likefirst.btos.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R

class MailRVAdapter (private val dataSet: Array<String>) : RecyclerView.Adapter<MailRVAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    private lateinit var mItemClickLister: MailItemClickListener
    interface MailItemClickListener{
        fun onClickItem()
    }

    fun setMyItemCLickLister(itemClickLister: MailItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mailView:ImageView  = view.findViewById(R.id.item_mailbox_iv)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_mailbox_rv, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.mailView.setOnClickListener { mItemClickLister.onClickItem() }
    }

    override fun getItemCount() = dataSet.size

}
