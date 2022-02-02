package com.likefirst.btos.ui.profile.plant

import android.media.Image
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentFlowerpotBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.response.PlantRequest
import com.likefirst.btos.data.remote.response.PlantResponse
import com.likefirst.btos.data.remote.service.PlantService
import com.likefirst.btos.data.remote.view.plant.PlantBuyView
import com.likefirst.btos.data.remote.view.plant.PlantSelectView
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.profile.ProfileFragment
import com.likefirst.btos.utils.toArrayList
import java.util.Comparator
import kotlin.collections.ArrayList

class PlantFragment :BaseFragment<FragmentFlowerpotBinding>(FragmentFlowerpotBinding:: inflate), MainActivity.onBackPressedListener  , PlantSelectView, PlantBuyView{

    var USERIDX=-1

    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        val Plants =loadData()
        val adapter = PlantRVAdapter(getPlantProfile(Plants))
        val plantSelectView :PlantSelectView =this
        val plantBuyView:PlantBuyView =this
        val userDB= UserDatabase.getInstance(requireContext())!!
        USERIDX=userDB.userDao().getUser().userIdx!!

        binding.flowerpotRv.adapter=adapter

        adapter.setMyItemCLickLister(object:PlantRVAdapter.PlantItemClickListener{
            override fun onClickInfoItem(plant:Plant){
                val plantItemFragment = PlantItemFragment()
                val bundle = Bundle()
                bundle.putParcelable("plantItem",plant)
                plantItemFragment.arguments=bundle
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fr_layout,plantItemFragment,"plantItem").addToBackStack(null)
                    .commit()

            }

            override fun onClickSelectItem(plant : Plant) {
                val plantService = PlantService()
                plantService.setPlantSelectView(plantSelectView)
                val request = PlantRequest(USERIDX,plant.plantIdx)
                plantService.selectPlant( request )
            }

            override fun onClickBuyItem(plant : Plant) {
                val plantService = PlantService()
                plantService.setPlantBuyView(plantBuyView)
                val request :PlantRequest = PlantRequest(USERIDX,plant.plantIdx)
                plantService.buyPlant(request)

            }
        })

        binding.flowerpotToolbar.toolbarBackIc.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()
        }

    }

   fun  loadData() : ArrayList<Plant> {
        val plantDB = PlantDatabase.getInstance(requireContext()!!)
        var list =plantDB?.plantDao()?.getPlants()!!
        list  =list.sortedWith(ComparePlant)
        val sorted=list.toArrayList()
        Log.d("PLANT/SORTED",sorted.toString())
        return sorted
    }

    fun getPlantProfile(plantList:ArrayList<Plant>):ArrayList<Pair<Plant,Int>>{
        val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        val activity = activity as MainActivity
        val ImageList = plantList.map{plant: Plant -> Pair<Plant, Int>(
            plant,
            requireContext()!!.resources.getIdentifier(
                plantName[plant.plantIdx-1]
                        +"_"+plant.maxLevel.toString()
                        +"_circle","drawable",
                activity.packageName)
        )
        }
        return ImageList.toArrayList()
    }


    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onPlantBuyError(Dialog: CustomDialogFragment) {
        Dialog.show(requireActivity().supportFragmentManager,"plantError")
    }

    override fun onPlantBuySuccess(plantIdx: Int, response : PlantResponse) {
        Log.d("Plantbuy/API","succes")
        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        plantDB.plantDao().setPlantStatus(plantIdx,"active")
        plantDB.plantDao().setPlantIsOwn(true,plantIdx)

    }

    override fun onPlantBuyFailure(code: Int, message: String) {
        Log.d("Plantbuy/API",message.toString())
    }


    override fun onPlantSelectError(Dialog: CustomDialogFragment) {
        Dialog.show(requireActivity().supportFragmentManager,"plantError")

    }

    override fun onPlantSelectSuccess(plantIdx: Int, request: PlantResponse) {
        Log.d("Plantselect/API",request.isSuccess.toString())

        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        val selected = plantDB.plantDao().getSelectedPlant("selected")!!
        plantDB.plantDao().setPlantStatus(selected.plantIdx,"active")
        plantDB.plantDao().setPlantStatus(plantIdx,"selected")
        Log.d("Plantselect/DB",plantDB.plantDao().getPlants().toString())

    }

    override fun onPlantSelectFailure(code: Int, message: String) {

    }
    class ComparePlant {
        companion object : Comparator<Plant> {
            override fun compare(a:Plant, b:Plant): Int {
                var p1=0
                var p2=0
                when(a.plantStatus){
                    "selected"-> p1= 10
                    "active"-> p1=3
                    "inactive"-> p1=2
                }
                when(b.plantStatus){
                    "selected"-> p2= 10
                    "active"-> p2=3
                    "inactive"-> p2=2
                }
                return p2-p1
            }
        }
    }

}

