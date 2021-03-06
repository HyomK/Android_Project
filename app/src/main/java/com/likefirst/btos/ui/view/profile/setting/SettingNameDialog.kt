package com.likefirst.btos.ui.view.profile.setting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.likefirst.btos.data.entities.UserName
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.CustomEditDialogBinding
import com.likefirst.btos.ui.view.main.CustomDialogFragment
import com.likefirst.btos.utils.saveUserName


class SettingNameDialog: DialogFragment(), SetSettingUserView {

    private var _binding: CustomEditDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CustomEditDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initDialog()
        return view
    }

    fun initDialog(){
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        val settingService = SettingUserService()

        binding.popupBody.text=arguments?.getString("bodyContext")
        val btnBundle = arguments?.getStringArray("btnData")

        binding.popupBtn1.setOnClickListener {
            buttonClickListener. onButton1Clicked()
            dismiss()
        }
        binding.popupBtn1.text=btnBundle?.get(0)

        binding.popupEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (null !=  binding.popupEdit.layout && binding.popupEdit.layout.lineCount > 1) {
                    binding.popupEdit.text.delete( binding.popupEdit.selectionStart - 1, binding.popupEdit.selectionStart)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO : ????????? ??????, ????????? ??????

            }
        })


        if(btnBundle?.size==1){
            binding.popupBtn2.visibility= View.GONE

        }else{
            binding.popupBtn2.setOnClickListener {
                if(binding.popupEdit.text.length <= 10){
                    //????????? ?????? api
                    var isSuccess =true

                    if(isSuccess){
                        settingService.setSettingUserView(this)
                        settingService.setName(userDB!!.getUserIdx(), UserName(binding.popupEdit.text.toString()))

                    }else{

                    }
                }else{
                    binding.popupError.text="10??? ????????? ??????????????????"
                    binding.popupError.visibility= View.VISIBLE
                }
            }
            binding.popupBtn2.text=btnBundle?.get(1)
        }
    }

    override fun onSetSettingUserViewLoading() {
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateNickName(binding.popupEdit.text.toString())
        Log.e("SETNAME",userDB.getUser().toString())
        binding.customDialogLayout.visibility=View.INVISIBLE
        saveUserName(binding.popupEdit.text.toString())

        val dialog = CustomDialogFragment()
        val data = arrayOf("??????")
        dialog.arguments= bundleOf(
            "bodyContext" to "??????????????? ?????????????????????.",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {
                dialog.dismiss()
                dismiss()
            }
            override fun onButton2Clicked() {}
        })
        dialog.show(this.parentFragmentManager, "settingSuccess")

    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.popupError.text=message
        binding.popupError.visibility= View.VISIBLE
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

    // ?????? ????????? ??????
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // ?????? ????????? ??????
    private lateinit var buttonClickListener: OnButtonClickListener

}