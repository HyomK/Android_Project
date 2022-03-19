package com.likefirst.btos.ui.view.profile.setting

import android.content.Intent
import androidx.core.os.bundleOf
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentAppinfoBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.MainActivity


class AppInfoFragment: BaseFragment<FragmentAppinfoBinding>(FragmentAppinfoBinding::inflate), MainActivity.onBackPressedListener   {
    override fun initAfterBinding() {
        binding.appInfoToolbar.toolbarBackIc.setOnClickListener{
            onBackPressed()
        }

        binding.appInfoToolbar.toolbarTitleTv.text="앱 정보"
        binding.appInfoTermsOfuse.setOnClickListener { 
          goToDetail("이용약관")
        }
        binding.appInfoPrivacyPolicy.setOnClickListener { 
            goToDetail("개인정보 정책")
        }
        binding.appInfoOpensource.setOnClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
//            goToDetail("오픈소스 라이센스")
        }
    }
    
    fun goToDetail(title : String){
        val bundle = bundleOf("title" to title)
        val detailFragment = AppInfoDetailFragment()
        detailFragment.arguments = bundle

        requireActivity()!!.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(R.id.fr_layout,detailFragment,"setAppDetail")
            .commit()
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}