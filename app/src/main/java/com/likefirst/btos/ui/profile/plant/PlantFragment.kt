package com.likefirst.btos.ui.profile.plant

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    lateinit var  sharedViewModel : SharedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel=ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
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

                val bundle =Bundle()
                bundle.putString("name",plant.plantName)
                bundle.putInt("level",plant.currentLevel)
                bundle.putInt("Id",plant.plantIdx)
                sharedViewModel.setLiveData(bundle)


            }

            override fun onClickBuyItem(plant : Pair<Plant,Int>):Pair<Plant,Int> {
                var newPlant = plant
                val buyDialog = CustomDialogFragment()
                val btn =arrayOf("취소","구매")
                val bundle = bundleOf(
                    "bodyContext" to "${plant.first.plantName} 화분을 구매하시겠습니까?",
                    "btnData" to btn
                )
                buyDialog.arguments=bundle
                buyDialog.setButtonClickListener(object:CustomDialogFragment.OnButtonClickListener{
                    override fun onButton1Clicked() {}
                    override fun onButton2Clicked() {
                        val plantService = PlantService()
                        plantService.setPlantBuyView(plantBuyView)
                        val request :PlantRequest = PlantRequest(USERIDX,newPlant.first.plantIdx)
                        plantService.buyPlant(request)
                        newPlant.first.plantStatus="active"
                        newPlant.first.isOwn=true
                        newPlant.first.currentLevel=0
                        val img= requireContext()!!.resources.getIdentifier(
                            plantName[newPlant.first.plantIdx-1]
                                    +"_0"
                                    +"_circle","drawable",
                            requireActivity().packageName)
                    }
                })
                buyDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
                return  newPlant
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
        return sorted
    }

    fun getPlantProfile(plantList:ArrayList<Plant>):ArrayList<Pair<Plant,Int>>{
        val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        val activity = activity as MainActivity
        val ImageList = plantList.map{plant: Plant -> Pair<Plant, Int>(
            plant,
            if(plant.isOwn){
                requireContext()!!.resources.getIdentifier(
                    plantName[plant.plantIdx-1]
                            +"_"+plant.currentLevel.toString()
                            +"_circle","drawable",
                    activity.packageName)
            }else{
                requireContext()!!.resources.getIdentifier(
                    plantName[plant.plantIdx-1]
                            +"_"+plant.maxLevel.toString()
                            +"_circle","drawable",
                    activity.packageName)

            })

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
        plantDB.plantDao().setPlantInit(plantIdx,"active",0,true)

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

