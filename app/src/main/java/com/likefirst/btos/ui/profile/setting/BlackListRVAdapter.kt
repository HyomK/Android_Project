package com.likefirst.btos.ui.profile.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R


class BlackListRVAdapter(var items: ArrayList<String> = arrayListOf()) : RecyclerView.Adapter<BlackListRVAdapter.ViewHolder>(){

    private lateinit var mItemClickLister:BlackListListener

    interface BlackListListener{
        fun onClick(name: String){}
        fun removeUser(pos :Int)
    }

    fun setOnListener(itemClickLister:BlackListListener){
        mItemClickLister= itemClickLister
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btn : Button = view.findViewById(R.id.blacklist_btn)
        val name : TextView = view.findViewById(R.id.blacklist_name)
        var status=true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blacklist, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=items[position]
        holder.btn.setOnClickListener {
            mItemClickLister.onClick(holder.name.text.toString())
            mItemClickLister.removeUser(position)
            removeItem(position)
        }
    }

    fun removeItem(pos:Int){
        if(pos>=0){
            items.removeAt(pos)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}