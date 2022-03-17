package com.likefirst.btos.presentation.view.profile.plant

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.likefirst.btos.databinding.CustomDialogLayoutBinding


class PlantDialog:DialogFragment(){
    private var _binding: CustomDialogLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  CustomDialogLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
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
                buttonClickListener.onButton2Clicked()
            }
            binding.popupBtn2.text=btnBundle?.get(1)
        }
    }


    interface OnButtonClickListener{
        fun onButton1Clicked()
        fun onButton2Clicked()
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