package com.likefirst.btos.ui.main



import androidx.core.os.bundleOf
import com.likefirst.btos.databinding.FragmentReportBinding
import com.likefirst.btos.ui.BaseFragment

class ReportFragment: BaseFragment<FragmentReportBinding>(FragmentReportBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        binding.reportToolbar.toolbarBackIc.setOnClickListener {
            mActivity.ChangeFragment().removeFragment(this)
        }
        binding.reportToolbar.toolbarTitleTv.text="신고하기"

        binding.reportDoneBtn.setOnClickListener{
            val dialog =CustomDialogFragment()
            val data = arrayOf("확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "신고 접수가 완료되었습니다. 쾌적한 컨텐츠를 위해 항상 노력하겠습니다.",
                "btnData" to data
            )
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                override fun onButton1Clicked() {
                    mActivity.supportFragmentManager.popBackStack()
                }
                override fun onButton2Clicked() {}
            })
            dialog.show(mActivity.supportFragmentManager, "CustomDialog")
        }

    }




}

