package com.likefirst.btos.ui.main



import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMainBinding.inflate
import com.likefirst.btos.databinding.FragmentReportBinding
import com.likefirst.btos.ui.BaseFragment

class ReportFragment: BaseFragment<FragmentReportBinding>(FragmentReportBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        binding.reportToolbar.toolbarBackIc.setOnClickListener {
            mActivity.changeFragment(this).backHome()
        }
        binding.reportToolbar.toolbarTitleTv.text="신고하기"



    }




}

