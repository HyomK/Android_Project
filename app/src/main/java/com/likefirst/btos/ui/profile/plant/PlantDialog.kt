package com.likefirst.btos.ui.profile.plant

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.remote.plant.view.SharedBuyModel
import com.likefirst.btos.databinding.CustomDialogLayoutBinding
import com.likefirst.btos.utils.errorDialog

class PlantDialog:DialogFragment(){
    private var _binding: CustomDialogLayoutBinding? = null
    private val binding get() = _binding!!
    lateinit var   sharedBuyModel: SharedBuyModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  CustomDialogLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        sharedBuyModel= ViewModelProvider(requireActivity()).get(SharedBuyModel::class.java)
        initDialog()
        return view
    }

    fun initDialog(){
        binding.popupBody.text=arguments?.getString("bodyContext")
        val btnBundle = arguments?.getStringArray("btnData")

        binding.popupBtn1.setOnClickListener {
            buttonClickListener. onButton1Clicked()
            dismiss()
        }
        binding.popupBtn1.text=btnBundle?.get(0)

        if(btnBundle?.size==1){
            binding.popupBtn2.visibility= View.GONE

        }else{
            binding.popupBtn2.setOnClickListener {
                val result = buttonClickListener.onButton2Clicked()
                val handler = android.os.Handler()
                handler.postDelayed({
                    Log.e("PlantAPI"," / buy : Dialogstart")
                    if(sharedBuyModel.isSuccess().value!!) sendData(result.first, result.second)
                    else{
                        Log.e("PlantAPI"," / Fail : DialogDismiss")
                            errorDialog().show(requireActivity().supportFragmentManager,"tag")
                    }
                    dismiss()
                }, 600)
            }
            binding.popupBtn2.text=btnBundle?.get(1)
        }
    }
    fun sendData(plant: Plant, res : Int){
        Log.e("PlantAPI"," / SEND DATA :"+ plant.toString())
        val bundle =Bundle()
        bundle.putString("plantName",plant.plantName)
        bundle.putString("status","active")
        bundle.putInt("plantIdx",plant.plantIdx)
        bundle.putInt("resId",res)
        sharedBuyModel.setLiveData(bundle)
        sharedBuyModel.setResult(true)

    }

    interface OnButtonClickListener{
        fun onButton1Clicked()
        fun onButton2Clicked():Pair<Plant,Int>
    }

    override fun onStart() {
        super.onStart();
        val lp : WindowManager.LayoutParams  =  WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        val window: Window = dialog!!.window!!
        window.attributes =lp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}