package com.likefirst.btos.ui.profile.setting

import android.os.ParcelFileDescriptor.open
import androidx.fragment.app.Fragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentAppinfoDetailBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import okio.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.channels.AsynchronousFileChannel.open


class AppInfoDetailFragment: BaseFragment<FragmentAppinfoDetailBinding>(FragmentAppinfoDetailBinding::inflate),MainActivity.onBackPressedListener {
    override fun initAfterBinding() {

        val title =  arguments?.getString("title","")

        binding.appDetailToolbar.toolbarTitleTv.text=title
        binding.appDetailToolbar.toolbarBackIc.setOnClickListener {
            onBackPressed()
        }


        when(title){
            "이용약관"->binding.appinfoDetailBodyTv.text= getString("term.text")
            "개인정보 정책"->binding.appinfoDetailBodyTv.text=getString("privacy_policy.text")
            "오픈소스 라이센스"->binding.appinfoDetailBodyTv.text=requireContext().getString(R.string.opensource)
        }

    }

    private fun getString(file:String): String? {
        var str = requireActivity().application.assets.open(file).bufferedReader().use{ it.readText() }
        return str
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}