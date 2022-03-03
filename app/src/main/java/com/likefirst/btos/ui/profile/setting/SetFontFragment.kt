package com.likefirst.btos.ui.profile.setting

import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserFont
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.FragmentFontBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class SetFontFragment:BaseFragment<FragmentFontBinding>(FragmentFontBinding::inflate)
    , MainActivity.onBackPressedListener, SetSettingUserView {
    private var fontSelect : Int = -1

    override fun initAfterBinding() {
        val settingService = SettingUserService()
        val userDatabase = UserDatabase.getInstance(requireContext())!!

        binding.fontToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
        binding.fontToolbar.toolbarTitleTv.text="폰트"

        val fontItem = resources.getStringArray(R.array.font)
        val recyclerViewAdapter = SetFontRecyclerViewAdapter(requireContext(),fontItem)
        binding.setFontRv.adapter = recyclerViewAdapter
        binding.setFontRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        recyclerViewAdapter.setMyItemClickListener(object : SetFontRecyclerViewAdapter.MyItemClickListener{
            override fun updateFont(fontIdx: Int) {
                fontSelect = fontIdx
                settingService.setSettingUserView(this@SetFontFragment)
                settingService.setFont(userDatabase.userDao().getUserIdx(), UserFont(fontSelect))
            }
        })

    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onSetSettingUserViewLoading() {
        setLoadingView()
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        binding.setFontLoadingPb.visibility = View.GONE
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        userDatabase.userDao().updateFontIdx(fontSelect)
        settingDialogRestart(requireActivity(),this, requireContext())
    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.setFontLoadingPb.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun setLoadingView(){
        binding.setFontLoadingPb.visibility=View.VISIBLE
        binding.setFontLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

}