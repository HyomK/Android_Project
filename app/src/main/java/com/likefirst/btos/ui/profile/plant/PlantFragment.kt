package com.likefirst.btos.ui.profile.plant

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentFlowerpotBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.remote.plant.response.PlantRequest
import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import com.likefirst.btos.data.remote.plant.view.PlantSelectView
import com.likefirst.btos.utils.ViewModel.SharedBuyModel
import com.likefirst.btos.utils.ViewModel.SharedSelectModel
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.utils.errorDialog
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.toArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.FieldPosition
import java.util.Comparator
import kotlin.collections.ArrayList

class PlantFragment :BaseFragment<FragmentFlowerpotBinding>(FragmentFlowerpotBinding:: inflate), MainActivity.onBackPressedListener  ,
    PlantSelectView, PlantBuyView {

    lateinit var  sharedSelectModel : SharedSelectModel
    lateinit var  sharedBuyModel : SharedBuyModel
    lateinit var plantName :Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedSelectModel=ViewModelProvider(requireActivity()).get(SharedSelectModel::class.java)
        sharedBuyModel=ViewModelProvider(requireActivity()).get(SharedBuyModel::class.java)
        plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!

    }
    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        val Plants =loadData()
        val adapter = PlantRVAdapter(getPlantProfile(Plants), sharedSelectModel, sharedBuyModel ,requireContext())
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
                val request = PlantRequest(getUserIdx(),plant.plantIdx)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.setPlantLoadingPb.visibility= View.VISIBLE
                    binding.flowerpotRv.isClickable=false
                    CoroutineScope(Dispatchers.IO).async {
                        plantService.selectPlant(request)
                    }.await()
                    adapter.selectItem(position)
                }

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
                    override fun onButton2Clicked(){
                        val origin = plant.first
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

                        val plantService = PlantService()
                        plantService.setPlantBuyView(plantBuyView)
                        val request :PlantRequest = PlantRequest(getUserIdx(),plant.first.plantIdx)

                        CoroutineScope(Dispatchers.Main).launch {
                            binding.setPlantLoadingPb.visibility= View.VISIBLE
                            binding.flowerpotRv.isClickable=false

                            CoroutineScope(Dispatchers.IO).async {
                                plantService.buyPlant(request)
                            }.await()
                            if(checking) adapter.buyItem(position, buyPlant)
                            buyDialog.dismiss()
                        }
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
        binding.setPlantLoadingPb.visibility= View.GONE
        binding.flowerpotRv.isClickable=true
        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        val plant = plantDB.plantDao().getPlant(plantIdx)
        if(plant!=null){
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

    override fun onPlantSelectLoading() {

    }

    override fun onPlantSelectSuccess(plantIdx: Int, request: PlantResponse) {
        binding.setPlantLoadingPb.visibility= View.GONE
        binding.flowerpotRv.isClickable=true
        Log.d("Plantselect/API",request.isSuccess.toString()) // acitve -> selected 변경
        val plantDB = PlantDatabase.getInstance(requireContext()!!)!!
        val plant = plantDB.plantDao().getPlant(plantIdx)!!
        val bundle =Bundle()
        bundle.putString("plantName",plant.plantName)
        bundle.putInt("level",plant.currentLevel)
        bundle.putInt("plantIdx",plant.plantIdx)
        sharedSelectModel.setLiveData(bundle)

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

