package com.likefirst.btos.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.response.Mailbox

class MailRVAdapter (private val dataSet:  ArrayList<Mailbox>) : RecyclerView.Adapter<MailRVAdapter.ViewHolder>() {

    private lateinit var mItemClickLister: MailItemClickListener
    interface MailItemClickListener{
        fun onClickItem(data:Mailbox)
    }

    fun setMyItemCLickLister(itemClickLister: MailItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mailView:ImageView  = view.findViewById(R.id.item_mailbox_iv)
        val textView: TextView = view.findViewById(R.id.item_mailbox_tv)
        lateinit var sendAt :String
        lateinit var type :String
        var idx : Int = 0



    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_mailbox_rv, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataSet[position].hasSealing){
            holder.mailView.setImageResource(R.drawable.envelope_locked)
        }else{
            holder.mailView.setImageResource(R.drawable.envelope)
        }
        holder.textView.text=dataSet[position].senderNickName
        holder.sendAt=dataSet[position].sendAt
        holder.idx=dataSet[position].idx
        holder.type=dataSet[position].type


        holder.mailView.setOnClickListener { mItemClickLister.onClickItem(dataSet[position]) }
    }

    override fun getItemCount() = dataSet.size

}
