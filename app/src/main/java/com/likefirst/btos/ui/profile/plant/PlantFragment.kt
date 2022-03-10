package com.likefirst.btos.ui.profile.plant

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentFlowerpotBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import com.likefirst.btos.data.remote.plant.view.PlantSelectView
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.data.remote.plant.viewmodel.PlantViewModel
import com.likefirst.btos.utils.NetworkConnection
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.toArrayList
import kotlinx.coroutines.*
import java.util.Comparator
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class PlantFragment :BaseFragment<FragmentFlowerpotBinding>(FragmentFlowerpotBinding:: inflate), MainActivity.onBackPressedListener  ,
    PlantSelectView, PlantBuyView {

    lateinit var plantName :Array<String>
    val plantSelectView : PlantSelectView =this
    val plantBuyView: PlantBuyView =this
    private val plantModel: PlantViewModel by viewModels()
    lateinit var plantAdapter : PlantRVAdapter
    lateinit var networkConnect : NetworkConnection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkConnect = NetworkConnection(requireContext())
        networkConnect.observe(viewLifecycleOwner) { isConnected ->
            run {
                if (isConnected == "false" || isConnected == "null") {
                    Log.e("network-msg",isConnected)
                     GlobalScope.launch {
                        Snackbar.make(binding.root,
                            "인터넷 연결을 확인해 주세요.", Snackbar.LENGTH_SHORT).show()
                       //TODO 네트워크 장애 처리
                    }
                }
            }
        }

        plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        initRecyclerView()
        plantModel.getPlantList().observe(viewLifecycleOwner,Observer{
                it-> run {
            plantAdapter.initData(getPlantProfile(loadData(it)))
            plantAdapter.notifyDataSetChanged()
            Log.e("plant_changed",it.toString())
        } })
        initAfterBinding()
    }
    override fun initAfterBinding() {
        val mActivity= activity as MainActivity
        initRecyclerView()
        binding.flowerpotToolbar.toolbarBackIc.setOnClickListener{
            mActivity.supportFragmentManager.popBackStack()
        }
        plantAdapter!!?.setMyItemCLickLister(object:PlantRVAdapter.PlantItemClickListener{
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

            @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
            override fun onClickSelectItem(plant : Plant,position:Int) {
                if( networkConnect.getConnectionState() == "true"){
                    CoroutineScope(Dispatchers.Main).launch {
                        setLoadingView()
                        binding.flowerpotRv.isClickable=false
                        plantModel.selectPlant(plantSelectView, getUserIdx(),plant.plantIdx).await()
                    }

                }else{
                    Snackbar.make(binding.root,
                        "인터넷 연결을 확인해 주세요.", Snackbar.LENGTH_SHORT).show()
                }

            }


            override fun onClickBuyItem(plant : Plant) {
                val buyDialog = PlantDialog()
                var checking = true
                val btn =arrayOf("취소","구매")
                val bundle = bundleOf(
                    "bodyContext" to "${plant.plantName} 화분을 구매하시겠습니까?",
                    "btnData" to btn
                )
                buyDialog.arguments=bundle
                buyDialog.setButtonClickListener(object:PlantDialog.OnButtonClickListener{
                    override fun onButton1Clicked() { checking = false}

                    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
                    override fun onButton2Clicked(){
                        if( networkConnect.getConnectionState() == "true"){
                            CoroutineScope(Dispatchers.Main).launch {
                                setLoadingView()
                                binding.flowerpotRv.isClickable=false
                                plantModel.buyPlant(plantBuyView,getUserIdx(),plant.plantIdx).await()
                                buyDialog.dismiss()
                            }

                        }else{
                            buyDialog.dismiss()
                            Snackbar.make(binding.root,
                                "인터넷 연결을 확인해 주세요.", Snackbar.LENGTH_SHORT).show()
                        }

                    }
                })
                buyDialog.show(requireActivity().supportFragmentManager, "PlantDialog")
            }
        })


    }

    fun  loadData( list : List<Plant>) : ArrayList<Plant> {
        val sortedList  =list.sortedWith(ComparePlant)
        return sortedList.toArrayList()
    }

    fun initRecyclerView(){
        plantAdapter= PlantRVAdapter(requireContext())
        binding.flowerpotRv.adapter=plantAdapter
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
        val mainActivity = activity as MainActivity
        if(mainActivity.isPlantOpen){
            mainActivity.onBottomNavHandler("prantrv",R.id.homeFragment)
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            return
        }
        mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        val mainActivity = activity as MainActivity
        mainActivity.isPlantOpen=false
    }

    override fun onPlantBuyError(Dialog: CustomDialogFragment) {
        Dialog.show(requireActivity().supportFragmentManager,"plantError")
    }

    override fun onPlantBuySuccess(plantIdx: Int, response : PlantResponse) {
        binding.setPlantLoadingPb.visibility= View.GONE
        binding.flowerpotRv.isClickable=true
        val plant = plantModel.getPlant(plantIdx)
        if(plant!=null){
            plantModel.setInitPlant(plantIdx,"active",0,true)
        }
    }


    override fun onPlantBuyFailure(code: Int, message: String) {
        when(code){
            1000-> Log.e( code.toString(),"화분 상태 변경에 실패하였습니다.")
            7011-> Log.e( code.toString(),"화분 선택에 실패하였습니다.")
            else ->Log.e( code.toString(),"데이터베이스 연결에 실패하였습니다")
        }
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
        val selected = plantModel.getSelectedPlant()
        plantModel.setPlantStatus(selected.plantIdx,"active")
        plantModel.setPlantStatus(plantIdx,"selected")

    }

    override fun onPlantSelectFailure(code: Int, message: String) {
        when(code){
            4000-> Log.e( code.toString(),"데이터베이스 연결에 실패하였습니다.")
            7010-> Log.e( code.toString(),"화분 상태 변경에 실패하였습니다.")
            else ->Log.e( code.toString(),"이미 선택된 화분입니다.")
        }
    }

    fun setLoadingView(){
        binding.setPlantLoadingPb.visibility= View.VISIBLE
        binding.setPlantLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
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