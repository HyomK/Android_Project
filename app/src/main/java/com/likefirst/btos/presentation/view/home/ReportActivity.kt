package com.likefirst.btos.presentation.view.home



import android.widget.Toast
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.notify.response.Report
import com.likefirst.btos.data.remote.notify.response.ReportResponse
import com.likefirst.btos.data.remote.notify.service.ReportService
import com.likefirst.btos.data.remote.notify.view.ReportView
import com.likefirst.btos.databinding.ActivityReportBinding
import com.likefirst.btos.presentation.BaseActivity
import com.likefirst.btos.presentation.view.main.CustomDialogFragment
import com.likefirst.btos.utils.errorDialog


class ReportActivity: BaseActivity<ActivityReportBinding>(ActivityReportBinding::inflate),ReportView {

    override fun initAfterBinding() {
        val type= intent.getStringExtra("type")
        val typeIdx = intent.getIntExtra("typeIdx",-1)
        var checkedOption = -1
        val reportService =ReportService()
        reportService.setReportView(this)

        binding.reportToolbar.toolbarBackIc.setOnClickListener {
            finish()
        }
        binding.reportToolbar.toolbarTitleTv.text="신고하기"
        binding.reportEdit.isFocusable=false
        binding.reportEdit.isClickable=false

        val index = resources.getStringArray(R.array.report_options)

        binding.reportItem1.itemReportTv.text=index[0]
        binding.reportItem2.itemReportTv.text=index[1]
        binding.reportItem3.itemReportTv.text=index[2]
        binding.reportItem4.itemReportTv.text=index[3]
        binding.reportItem5.itemReportTv.text=index[4]

        binding.reportItem1.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            checkedOption=0
            binding.reportItem1.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem2.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            checkedOption=1
            binding.reportItem2.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }
        binding.reportItem3.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            checkedOption=2
            binding.reportItem3.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem4.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            checkedOption=3
            binding.reportItem4.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }

        binding.reportItem5.itemReportLayout.setOnClickListener {
            checkBoxHandler(binding)
            binding.reportEdit.isFocusableInTouchMode=true
            binding.reportEdit.isClickable=true
            checkedOption=4
            binding.reportItem5.itemReportIv.setImageResource(R.drawable.ic_check_true)
        }




       binding.reportDoneBtn.setOnClickListener{
            val dialog = CustomDialogFragment()
            val data = arrayOf("취소","확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "신고 접수를 진행하히겠습니까?",
                "btnData" to data
            )
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked() {

                }
                override fun onButton2Clicked() {
                    if(checkedOption==-1){
                        Toast.makeText(this@ReportActivity,"신고 사유를 선택해 주세요",Toast.LENGTH_SHORT).show()
                        return
                    }

                    val report = Report(type!!,index[checkedOption],typeIdx, binding.reportEdit.text.toString() )
                    reportService.sendReport(report)
                    finish()
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")
        }

    }


    fun checkBoxHandler( binding:ActivityReportBinding) {
        binding.reportItem1.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem2.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem3.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem4.itemReportIv.setImageResource(R.drawable.ic_check_false)
        binding.reportItem5.itemReportIv.setImageResource(R.drawable.ic_check_false)
    }

    override fun onReportSuccess(response: ReportResponse) {
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "신고 접수가 완료되었습니다. 쾌적한 컨텐츠를 위해 항상 노력하겠습니다.",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {
                finish()
            }
            override fun onButton2Clicked() {}
        })
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    override fun onReportFailure(code: Int, message: String) {
        errorDialog().show(supportFragmentManager,"Report Error")
    }


}

