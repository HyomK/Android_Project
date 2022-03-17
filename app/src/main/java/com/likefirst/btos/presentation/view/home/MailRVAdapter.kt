package com.likefirst.btos.presentation.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.posting.response.Mailbox

class MailRVAdapter () : RecyclerView.Adapter<MailRVAdapter.ViewHolder>() {

    private lateinit var mItemClickLister: MailItemClickListener
    private var dataSet=  ArrayList<Mailbox>()

    interface MailItemClickListener{
        fun onClickItem(data:Mailbox, position: Int)
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


        holder.mailView.setOnClickListener { mItemClickLister.onClickItem(dataSet[position], position) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position : Int){
        dataSet.removeAt(position)
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun initData( data:  ArrayList<Mailbox>){
        dataSet.clear()
        dataSet.addAll(data)
    }

    override fun getItemCount() = dataSet.size

}
