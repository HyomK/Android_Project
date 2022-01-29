package com.likefirst.btos.ui.main



import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentReportBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.home.MailViewFragment

class ReportFragment: BaseFragment<FragmentReportBinding>(FragmentReportBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        binding.reportToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.reportToolbar.toolbarTitleTv.text="신고하기"

        val index = arrayOf("스팸", "성적 컨텐츠","혐오 발언 또는 괴롭힘", "마음에 들지 않습니다", "기타")

        binding.reportItem1.itemReportTv.text="스팸"
        binding.reportItem2.itemReportTv.text="성적 컨텐츠"
        binding.reportItem3.itemReportTv.text="혐오 발언 또는 괴롭힘"
        binding.reportItem4.itemReportTv.text="마음에 들지 않습니다"
        binding.reportItem5.itemReportTv.text="기타"

        binding.reportItem1.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportItem1.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem2.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportItem2.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }
        binding.reportItem3.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportItem3.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem4.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportItem4.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem5.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportItem5.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }



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


    fun checkBoxHandler( binding: FragmentReportBinding) {
        binding.reportItem1.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem2.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem3.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem4.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem5.itemReportIv.setImageResource(R.drawable.ic_check_false)
    }



}

