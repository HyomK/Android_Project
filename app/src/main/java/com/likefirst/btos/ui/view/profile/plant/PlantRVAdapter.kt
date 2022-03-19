package com.likefirst.btos.ui.view.profile.plant

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.module.Plant
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class PlantRVAdapter(val context : Context) : RecyclerView.Adapter<PlantRVAdapter.ViewHolder>() {

    private lateinit var mItemClickLister: PlantItemClickListener
    private var dataSet= ArrayList<Pair<Plant,Int>>()



    interface PlantItemClickListener{
        fun onClickInfoItem(plant : Plant)
        fun onClickSelectItem(plant : Plant, position:Int)
        fun onClickBuyItem(plant : Plant)
    }

    fun setMyItemCLickLister(itemClickLister: PlantItemClickListener){
        mItemClickLister=itemClickLister
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantName : TextView = view.findViewById(R.id.flowerpot_name_tv)
        val plantLevel : TextView = view.findViewById(R.id.flowerpot_level_tv)
        val plantImage : ImageView = view.findViewById(R.id.flowerpot_iv)
        val layout : ConstraintLayout = view.findViewById(R.id.item_flowerpot_layout)
        val selectBtn : Button = view.findViewById(R.id.flowerpot_select_btn )
        val maxIv : TextView = view.findViewById(R.id.flowerpot_max_tv)
        val status  : TextView = view.findViewById(R.id.flowerpot_status_tv)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_flowerpot, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        val plant = item.first
        val won = NumberFormat.getCurrencyInstance(Locale.KOREA).format(plant.plantPrice)

        holder.plantName.text= item.first.plantName
        holder.plantImage.setImageResource(item.second)

       if(plant.plantStatus=="inactive"){ //미보유 아이템
           holder.layout.setBackgroundResource(R.drawable.profile_shop_bg)
           holder.plantLevel.text=plant.maxLevel.toString()+"단계"
           holder.status.visibility=View.GONE
           holder.selectBtn.visibility=View.VISIBLE
           holder.selectBtn.text=won
           holder.maxIv.setBackgroundResource(R.drawable.ic_max_gray_bg)
           holder.selectBtn.setOnClickListener{
               mItemClickLister.onClickBuyItem(dataSet[position].first)
           }
       }else {
           holder.layout.setBackgroundResource(R.drawable.profile_bg)
           holder.plantLevel.text=plant.currentLevel.toString()+"단계"
           holder.maxIv.setBackgroundResource(R.drawable.ic_max_bg)
           holder.maxIv.visibility= if(plant.currentLevel == plant.maxLevel) View.VISIBLE else View.GONE
           holder.status.visibility=View.VISIBLE

           if(plant.plantStatus=="selected"){
               holder.selectBtn.visibility=View.GONE
               holder.status.visibility=View.VISIBLE
           }else{ //보유 아이템
               holder.status.visibility==View.GONE
               holder.selectBtn.visibility=View.VISIBLE
               holder.selectBtn.text="선택"
               holder.selectBtn.setOnClickListener{
                   mItemClickLister.onClickSelectItem(dataSet[position].first ,position)

               }
           }
       }
        holder.plantImage.setOnClickListener { mItemClickLister.onClickInfoItem(dataSet[position].first)  }
    }

    override fun getItemCount(): Int {
       return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData (initData :ArrayList<Pair<Plant,Int>>){
        dataSet.clear()
        dataSet.addAll(initData)
        notifyDataSetChanged()
    }



    @SuppressLint("NotifyDataSetChanged")
    fun reset(origin:ArrayList<Pair<Plant,Int>>){
        val newData =origin.sortedWith(ComparePlant)
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }


    class ComparePlant {
        companion object : Comparator<Pair<Plant,Int>> {
            override fun compare(a:Pair<Plant,Int>, b:Pair<Plant,Int>): Int {
                var p1=0
                var p2=0
                when(a.first.plantStatus){
                    "selected"->p1=2
                    "active"-> p1=2
                    "inactive"-> p1=1
                }
                when(b.first.plantStatus){
                    "selected"->p2=2
                    "active"-> p2=2
                    "inactive"-> p2=1
                }
                return p2-p1
            }
        }
    }

}