package com.likefirst.btos.ui.profile.plant

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentFlowerpotBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.plant.response.PlantRequest
import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import com.likefirst.btos.data.remote.plant.view.PlantSelectView
import com.likefirst.btos.data.remote.plant.view.SharedBuyModel
import com.likefirst.btos.data.remote.plant.view.SharedSelectModel
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.utils.errorDialog
import com.likefirst.btos.utils.toArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Comparator
import kotlin.collections.ArrayList

class PlantFragment :BaseFragment<FragmentFlowerpotBinding>(FragmentFlowerpotBinding:: inflate), MainActivity.onBackPressedListener  ,
    PlantSelectView, PlantBuyView {

    var USERIDX=-1
    lateinit var  sharedSelectModel : SharedSelectModel
    lateinit var  sharedBuyModel : SharedBuyModel
    lateinit var plantName :Array<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedSelectModel=ViewModelProvider(requireActivity()).get(SharedSelectModel::class.java)
        sharedBuyModel=ViewModelProvider(requireActivity()).get(SharedBuyModel::class.java)
        plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        val userDB= UserDatabase.getInstance(requireContext())!!
        USERIDX=userDB.userDao().getUser().userIdx!!
    }

    override fun initAfterBinding() {
        val mActivity= activity as MainActivity

        val Plants =loadData()
        val adapter = PlantRVAdapter(getPlantProfile(Plants), sharedSelectModel, sharedBuyModel)
        val plantSelectView : PlantSelectView =this
        val plantBuyView: PlantBuyView =this

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

            override fun onClickSelectItem(plant : Plant,position:Int) {
                val plantService = PlantService()
                plantService.setPlantSelectView(plantSelectView)
                val request = PlantRequest(USERIDX,plant.plantIdx)
                plantService.selectPlant( request )

                val handler = android.os.Handler()
                handler.postDelayed({
                    if(sharedSelectModel.isSuccess().value==true)
                        Toast.makeText(requireActivity(),"화분이 변경되었습니다",Toast.LENGTH_SHORT).show()
                    else
                        errorDialog().show(requireActivity().supportFragmentManager,"")

                    adapter.selectItem(position)
                }, 600)

                sharedSelectModel.setResult(false)
            }


            override fun onClickBuyItem(plant : Pair<Plant,Int> , position: Int) {
                var buyPlant : Pair<Plant,Int> = plant
                val buyDialog = PlantDialog()
                var checking = true
                val btn =arrayOf("취소","구매")
                val bundle = bundleOf(
                    "bodyContext" to "${plant.first.plantName} 화분을 구매하시겠습니까?",
                    "btnData" to btn
                )
                buyDialog.arguments=bundle
                buyDialog.setButtonClickListener(object:PlantDialog.OnButtonClickListener{
                    override fun onButton1Clicked() { checking = false}
                    override fun onButton2Clicked():Pair<Plant,Int> {
                        val origin = plant.first
                        val handler = android.os.Handler()
                        val plantService = PlantService()
                        plantService.setPlantBuyView(plantBuyView)
                        val request :PlantRequest = PlantRequest(USERIDX,plant.first.plantIdx)
                        plantService.buyPlant(request)
                        val img= requireContext()!!.resources.getIdentifier(
                            plantName[ origin.plantIdx-1]
                                    +"_0"
                                    +"_circle","drawable",
                            requireActivity().packageName)
                        var newPlant = plant.first  // 구매선택한 식물
                        newPlant.plantStatus="active" //active로 수정
                        newPlant.isOwn=true  //소유로 수정
                        newPlant.currentLevel=0
                        buyPlant= Pair(newPlant,img) //바뀐 내용 return
                        handler.postDelayed({
                            if(checking) adapter.buyItem(position)
                        }, 700)
                        Log.e("PlantAPI"," / buyPlant result ${buyPlant}")
                        return buyPlant!! //
                    }
                })
                buyDialog.show(requireActivity().supportFragmentManager, "PlantDialog")
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
        sharedBuyModel.setResult(false)
    }

    override fun onPlantBuySuccess(plantIdx: Int, response : PlantResponse) {
        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        val plant = plantDB.plantDao().getPlant(plantIdx)
        if(plant!=null){
            sharedBuyModel.setResult(true)  // 성공 전달
            plantDB.plantDao().setPlantInit(plantIdx,"active",0,true)

        }

    }


    override fun onPlantBuyFailure(code: Int, message: String) {
        when(code){
            1000-> Log.e( code.toString(),"화분 상태 변경에 실패하였습니다.")
            7011-> Log.e( code.toString(),"화분 선택에 실패하였습니다.")
            else ->Log.e( code.toString(),"데이터베이스 연결에 실패하였습니다")
        }
        sharedBuyModel.setResult(false)
    }


    override fun onPlantSelectError(Dialog: CustomDialogFragment) {
        Dialog.show(requireActivity().supportFragmentManager,"plantError")

    }

    override fun onPlantSelectSuccess(plantIdx: Int, request: PlantResponse) {
        Log.d("Plantselect/API",request.isSuccess.toString()) // acitve -> selected 변경
        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        sharedSelectModel.setResult(true)

        val plant = plantDB.plantDao().getPlant(plantIdx)!!
        if(sharedSelectModel.isSuccess().value==true){
            val bundle =Bundle()
            bundle.putString("plantName",plant.plantName)
            bundle.putInt("level",plant.currentLevel)
            bundle.putInt("plantIdx",plant.plantIdx)
            sharedSelectModel.setLiveData(bundle)
        }else{
            errorDialog().show(requireActivity().supportFragmentManager,"selectError")
        }
        val selected = plantDB.plantDao().getSelectedPlant()!!
        plantDB.plantDao().setPlantStatus(selected.plantIdx,"active")
        plantDB.plantDao().setPlantStatus(plantIdx,"selected")


    }

    override fun onPlantSelectFailure(code: Int, message: String) {
        when(code){
            4000-> Log.e( code.toString(),"데이터베이스 연결에 실패하였습니다.")
            7010-> Log.e( code.toString(),"화분 상태 변경에 실패하였습니다.")
            else ->Log.e( code.toString(),"이미 선택된 화분입니다.")
        }
        sharedSelectModel.setResult(false)
    }

    class ComparePlant {
        companion object : Comparator<Plant> {
            override fun compare(a:Plant, b:Plant): Int {
                var p1=0
                var p2=0
                when(a.plantStatus){
                    "selected"->p1=2
                    "active"-> p1=2
                    "inactive"-> p1=1
                }
                when(b.plantStatus){
                    "selected"->p2=2
                    "active"-> p2=2
                    "inactive"-> p2=1
                }
                return p2-p1
            }
        }
    }

}

