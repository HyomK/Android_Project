package com.likefirst.btos.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R

class MailRVAdapter (private val dataSet: Array<String>) : RecyclerView.Adapter<MailRVAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    interface MailItemClickListener{
        fun onClickItem()
    }
    private lateinit var mItemClickLister: MailItemClickListener

    fun setMyItemCLickLister(itemClickLister: MailItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mailView:ImageView

        init {
            // Define click listener for the ViewHolder's View.
            mailView = view.findViewById(R.id.item_mailbox_iv)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_mailbox_rv, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.mailView.setOnClickListener { mItemClickLister.onClickItem() }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
