package com.likefirst.btos.ui.profile.setting

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserBirth
import com.likefirst.btos.data.entities.UserName
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.CustomEditDialogBinding
import com.likefirst.btos.databinding.SettingBirthDialogBinding
import com.likefirst.btos.ui.main.CustomDialogFragment

class SettingBirthDialog : DialogFragment(), SetSettingUserView {

    private var _binding: SettingBirthDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SettingBirthDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initDialog()
        return view
    }

    fun initDialog(){
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        val settingService = SettingUserService()
        var birth: Int =userDB!!.getUser().birth
      //  binding.birthTill.isHintEnabled=false
        if(birth == 0)  binding.birthTill.hint="선택안함"
        else binding.birthTill.hint=birth.toString()

        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.onboarding_dropdown_item,agelist)
        binding.birthList.setAdapter(arrayAdapter)
        binding.birthList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.birthList.dropDownHeight=300
        binding.birthList.setOnClickListener {
            binding.birthTill.isHintEnabled=false
            binding.birthList.showDropDown()
        }
        binding.birthTill.setEndIconOnClickListener {
            binding.birthTill.isHintEnabled=false
            binding.birthList.showDropDown()
        }





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
                if(binding.birthList.text.toString()=="선택안함"){
                    binding.birthError.text="생년을 선택해주세요."
                    binding.birthError.visibility= View.VISIBLE
                }
                else{
                    settingService.setSettingUserView(this)
                    settingService.setBirth(userDB!!.getUserIdx(), UserBirth(binding.birthList.text.toString().toInt()))
                }
            }
            binding.popupBtn2.text=btnBundle?.get(1)
        }
    }

    override fun onSetSettingUserViewLoading() {
        setLoadingView()
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        binding.birthLoadingPb.visibility = View.GONE
        binding.customDialogLayout.visibility = View.INVISIBLE
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateBirth(binding.birthList.text.toString().toInt())
        Log.e("SETBIRTH",userDB.getUser().toString())
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "성공적으로 변경되었습니다.",
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
        binding.birthLoadingPb.visibility = View.GONE
        binding.birthError.text=message
        binding.birthError.visibility= View.VISIBLE
    }


    interface OnButtonClickListener{
        fun onButton1Clicked()
        fun onButton2Clicked()

    }
    fun setLoadingView(){
        binding.birthLoadingPb.visibility= View.VISIBLE
        binding.birthLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
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

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener

}