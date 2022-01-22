package com.likefirst.btos.ui.fragment.plant

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.likefirst.btos.ApplicationClass.Companion.TAG
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentFlowerpotBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.home.HomeFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.Plant
import com.likefirst.btos.utils.PlantDatabase
import com.likefirst.btos.utils.PlantItem
import org.json.JSONObject
import kotlin.collections.ArrayList

class PlantFragment :BaseFragment<FragmentFlowerpotBinding>(FragmentFlowerpotBinding:: inflate) {
    override fun initAfterBinding() {
        val presFragment = this
        val mActivity= activity as MainActivity
        val flowerpots = loadData()
        val adapter = PlantRVAdapter(flowerpots)
        var isPop = false

        binding.flowerpotRv.adapter=adapter



        adapter.setMyItemCLickLister(object:PlantRVAdapter.PlantItemClickListener{
            override fun onClickShopItem(){
                Log.d("RV SELECT","clicked")
                mActivity.supportFragmentManager
                    .beginTransaction()
                    .add(R.id.flowerpot_popup_layout,PlantItemFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })


        binding.flowerpotToolbar.toolbarBackIc.setOnClickListener{
            mActivity.ChangeFragment().moveFragment(R.id.fr_layout,HomeFragment())
        }

    }


   fun  loadData() : ArrayList<Plant> {
        val assetManager = resources.assets
        val gson=Gson()
        val plantDB = PlantDatabase.getInstance(requireContext()!!)
        val plantObjType = object: TypeToken<Plant>() {}.type
        var inputStream= assetManager.open("PlantDummy.json")
        var jsonString = inputStream.bufferedReader().use { it.readText() }
        var jObject = JSONObject(jsonString)
        var jArray = jObject.getJSONArray("result")
        var userPlantList= ArrayList<Plant>()
        var plantItems= ArrayList<PlantItem>()


        for (i in 0 until jArray.length()) {
            val obj = jArray.getJSONObject(i)
            val plantObj = gson.fromJson(obj.toString() , Plant::class.java)
            userPlantList.add(plantObj)
         }

        inputStream= assetManager.open("PlantItems.json")
        jsonString = inputStream.bufferedReader().use { it.readText() }
        jObject = JSONObject(jsonString)
        jArray = jObject.getJSONArray("items")

       for (i in 0 until jArray.length()) {
           val obj = jArray.getJSONObject(i)
           val plantObj = gson.fromJson(obj.toString() , PlantItem::class.java)
           plantItems.add(plantObj)
       }

        Log.d(TAG, "roomDB: $userPlantList" )

        plantItems.forEach { i ->
           run {
               if (plantDB?.plantDao()?.getPlant(i.plantIdx) == null) {
                   plantDB?.plantDao()?.insert(i)
               } else {
                   plantDB?.plantDao()?.update(i)
               }
           }
        }  // 전체 화분 목록 DB 업데이트

        val totItemIdx = plantItems.map{i -> i.plantIdx}
        val userItemIdx = userPlantList.map{i -> i.plantIdx}
        val shopItemIdx = totItemIdx.subtract(userItemIdx) //차집합
        val shopList =setShopItem(shopItemIdx)




       userPlantList.addAll(shopList) // 두개 붙이기
       Log.d(TAG, "shopItemIdx: $userPlantList" )
       return userPlantList

    }

    fun setShopItem(shopItemIdx : Set<Int>) : ArrayList<Plant>{

        val plantDB = PlantDatabase.getInstance(requireContext()!!)
        val shopList = ArrayList<Plant>()
        shopItemIdx.forEach { it ->
            run {
                val item = plantDB?.plantDao()?.getPlant(it)!!
                val plant =
                    Plant(item.plantIdx, item.plantName, "", item.plantPrice, item.maxLevel, 0, "")
                shopList.add(plant)
            }
        }

        return shopList

    }

//    fun setUserItem(userItemIdx : ArrayList<Int>) : ArrayList<Plant>{
//
//        val plantDB = PlantDatabase.getInstance(requireContext()!!)
//        val userList = ArrayList<Plant>()
//        userItemIdx.forEach { it ->{
//            val item = plantDB?.plantDao()?.getPlant(it)!!
//            val plant= Plant(item.plantIdx,item.plantName, "",item.plantPrice, item.maxLevel, 0,"")
//            userList.add(plant)
//        }
//        }
//
//        return userList
//
//    }




}

