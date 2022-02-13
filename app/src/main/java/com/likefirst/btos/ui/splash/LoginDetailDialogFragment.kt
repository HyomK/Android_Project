package com.likefirst.btos.ui.splash

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.LoginDetailDialogBinding


class LoginDetailDialogFragment : DialogFragment() {
    private var _binding: LoginDetailDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LoginDetailDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initDialog()
        return view
    }

    fun initDialog(){
       val content = arguments?.getString("content","")!!
       binding.loginDetailDialogBody.text=content

        binding.loginDetailDialogBtn.setOnClickListener{
           dismiss()
       }
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

}