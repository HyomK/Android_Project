package com.likefirst.btos.ui.profile.plant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.utils.resourceFilter
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PlantRVAdapter( val dataSet: List<Plant> , val initImage :List<Int>) : RecyclerView.Adapter<PlantRVAdapter.ViewHolder>() {

    private lateinit var mItemClickLister: PlantItemClickListener


    interface PlantItemClickListener{
        fun onClickInfoItem(plant : Plant)
        fun onClickSelectItem(plant : Plant)
        fun onClickBuyItem(plant : Plant)
    }

    fun setMyItemCLickLister(itemClickLister: PlantItemClickListener){
        mItemClickLister=itemClickLister
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        val won = NumberFormat.getCurrencyInstance(Locale.KOREA).format(item.plantPrice)

        holder.plantName.text= item.plantName
        holder.plantImage.setImageResource(initImage[position])


       if(item.isOwn==false){ //미보유 아이템
           holder.layout.setBackgroundResource(R.drawable.profile_shop_bg)
           holder.plantLevel.text=item.maxLevel.toString()+"단계"
           holder.status.visibility=View.GONE
           holder.selectBtn.visibility=View.VISIBLE
           holder.selectBtn.text=won
           holder.maxIv.setBackgroundResource(R.drawable.ic_max_gray_bg)
           holder.selectBtn.setOnClickListener{
             buyItem(position)
           }
       }else {
           holder.layout.setBackgroundResource(R.drawable.profile_bg)
           holder.plantLevel.text=item.currentLevel.toString()+"단계"
           holder.maxIv.setBackgroundResource(R.drawable.ic_max_bg)
           holder.maxIv.visibility= if(item.currentLevel == item.maxLevel) View.VISIBLE else View.GONE

           if(item.plantStatus=="selected"){
               holder.status.visibility==View.VISIBLE
               holder.selectBtn.visibility=View.GONE

           }else{ //보유 아이템
               holder.status.visibility==View.GONE
               holder.selectBtn.visibility=View.VISIBLE
               holder.selectBtn.text="선택"
               holder.selectBtn.setOnClickListener{
                //  mItemClickLister.onClickSelectItem(dataSet[position])
                   selectItem(position)
               }
           }
       }
        holder.plantImage.setOnClickListener { mItemClickLister.onClickInfoItem(dataSet[position])  }
    }

    override fun getItemCount(): Int {
       return dataSet.size
    }
    fun buyItem(position :Int){
        mItemClickLister.onClickBuyItem(dataSet[position])
        dataSet[position].plantStatus="active"
        dataSet[position].isOwn=true
        dataSet[position].currentLevel=0
        notifyItemChanged(position)
    }

    fun selectItem(position: Int){
        mItemClickLister.onClickSelectItem(dataSet[position])
        dataSet.forEachIndexed { index, plant ->
            run {
                if (plant.plantStatus == "selected") {
                    dataSet[index].plantStatus = "active"
                    notifyDataSetChanged()

                }
            }
        }

        dataSet[position].plantStatus="selected"
        if(dataSet[position].currentLevel==-1) dataSet[position].currentLevel=0
        //notifyItemChanged(position)
        notifyDataSetChanged()

    }



}