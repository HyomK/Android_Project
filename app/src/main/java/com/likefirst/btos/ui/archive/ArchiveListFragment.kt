package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.viewer.response.ArchiveListPageInfo
import com.likefirst.btos.data.remote.viewer.response.ArchiveListResult
import com.likefirst.btos.data.remote.viewer.service.ArchiveListService
import com.likefirst.btos.data.remote.viewer.view.ArchiveListView
import com.likefirst.btos.databinding.FragmentArchiveListBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.utils.getUserIdx
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ArchiveListFragment : BaseFragment<FragmentArchiveListBinding>(FragmentArchiveListBinding::inflate), ArchiveListView{

    override fun initAfterBinding() {
        binding.archiveListToolbar.archiveListPeriodIv.setOnClickListener {
            it.isSelected = !it.isSelected
            val periodDialog = ArchiveListPeriodDialog()
            periodDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
            periodDialog.show(childFragmentManager, periodDialog.tag)
        }
        loadDiaryList(1)
    }

    fun loadDiaryList(pageNum : Int){
        val query = HashMap<String,String>()
        query["startDate"] = "2022.01.01"
        query["endDate"] = "2022.02.01"
        val archiveListService = ArchiveListService()
        archiveListService.setArchiveListView(this)
        archiveListService.getList(getUserIdx(), pageNum, query)
    }

    override fun onArchiveListLoading() {
    }

    override fun onArchiveListSuccess(
        result: ArrayList<ArchiveListResult>,
        pageInfo: ArchiveListPageInfo
    ) {
        // result를 List RecyclerView에 뿌릴 diaryList로 변환
        val diaryList = arrayListOf<Any>()
        for (item in result){
            diaryList.add(item.month)
            for (i in 0 until item.diaryList.size){
                diaryList.add(item.diaryList[i])
            }
        }

        val mAdapter = ArchiveListRVAdapter(diaryList, requireContext())
        val mDecoration = ArchiveListItemDecoration()
        mDecoration.setSize(requireContext())
        binding.archiveListRv.apply {
            adapter = mAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(mDecoration)
        }
    }

    override fun onArchiveListFailure(code: Int) {
    }
}