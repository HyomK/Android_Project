package com.likefirst.btos.ui.splash

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.likefirst.btos.databinding.LoginDialogBinding


class LoginDialogFragment(): DialogFragment() {
    private var _binding: LoginDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  LoginDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initDialog()
        return view
    }

    fun initDialog(){
      binding.loginDialogAllCheck.loginDialogItemTitle.text="모두 동의합니다"
      binding.loginDialogAllCheck.loginDialogItemLook.visibility=View.GONE

      binding.loginDialogNextBtn.setOnClickListener{
          if(binding.loginDialogAllCheck.loginDialogCheckbox.isChecked) {
              dismiss()
          }
          else Toast.makeText(requireContext(),"필수 약관에 동의해주세요",Toast.LENGTH_SHORT).show()
      }
      binding.loginDialogPrivacy.loginDialogItemTitle.text="[필수] 개인정보 수집 동의"
      binding.loginDialogUse.loginDialogItemTitle.text="[필수] 서비스 이용약관"

      binding.loginDialogPrivacy.loginDialogCheckbox.setOnClickListener{
         onCheckChanged( "single")
      }

      binding.loginDialogUse.loginDialogCheckbox.setOnClickListener{
          onCheckChanged("single")
      }
      binding.loginDialogAllCheck.loginDialogCheckbox.setOnClickListener{
          onCheckChanged("all")
      }

      binding.loginDialogUse.loginDialogItemLook.setOnClickListener {
          val dialog = LoginDetailDialogFragment()
          val bundle = bundleOf("content" to getString("term.text"))
          dialog.arguments=bundle
          Log.e("LOGINDIALOG",dialog.arguments.toString())
          dialog.show(requireActivity().supportFragmentManager, "")
      }
      binding.loginDialogPrivacy.loginDialogItemLook.setOnClickListener {
          val dialog = LoginDetailDialogFragment()
          val bundle = bundleOf("content" to getString("privacy_policy.text"))
          dialog.arguments=bundle
          Log.e("LOGINDIALOG",dialog.arguments.toString())
          dialog.show(requireActivity().supportFragmentManager, "")
      }

    }
    private fun getString(file:String): String? {
        var str = requireActivity().application.assets.open(file).bufferedReader().use{ it.readText() }
        return str
    }

    private fun onCheckChanged(option : String) {
        when(option) {
           "all" -> {
                if (binding.loginDialogAllCheck.loginDialogCheckbox.isChecked) {
                    binding.loginDialogUse.loginDialogCheckbox.isChecked=true
                    binding.loginDialogPrivacy.loginDialogCheckbox.isChecked = true

                }else {
                    binding.loginDialogUse.loginDialogCheckbox.isChecked = false
                    binding.loginDialogPrivacy.loginDialogCheckbox.isChecked = false

                }
            }
            else -> {
                binding.loginDialogAllCheck.loginDialogCheckbox.isChecked = (
                        binding.loginDialogUse.loginDialogCheckbox.isChecked
                        &&binding.loginDialogPrivacy.loginDialogCheckbox.isChecked)
            }
        }
    }

    interface OnButtonClickListener{
        fun onButtonClicked()
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
        Log.e("LOGINDIALOG","destroy")
        val activity = activity as OnboardingActivity
        val intent = Intent(requireActivity(),LoginActivity::class.java)
        dismiss()
        activity.finish()
        startActivity(intent)
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}