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

    companion object{
        var requiredPageNum = 1
        var lastDate = ""
    }

    override fun initAfterBinding() {
        //companion object 초기화
        requiredPageNum = 1
        lastDate = ""
        
        //datePicker 기능 설정
        binding.archiveListToolbar.archiveListPeriodIv.setOnClickListener {
            it.isSelected = !it.isSelected
            val periodDialog = ArchiveListPeriodDialog()
            periodDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
            periodDialog.show(childFragmentManager, periodDialog.tag)
        }
        initDiaryList()
    }

    fun initDiaryList(){
        val mAdapter = ArchiveListRVAdapter(requireContext())
        val mDecoration = ArchiveListItemDecoration()
        mDecoration.setSize(requireContext())
        binding.archiveListRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.archiveListRv.canScrollVertically(1)) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        mAdapter.deleteLoading()
                        mAdapter.notifyItemRemoved(mAdapter.itemCount)
                        binding.archiveListRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        loadDiaryList(requiredPageNum, mAdapter)
                    }
                }
            }
        })
        binding.archiveListRv.apply {
            adapter = mAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(mDecoration)
        }
        loadDiaryList(requiredPageNum, mAdapter)
    }

    fun loadDiaryList(pageNum : Int, adapter : ArchiveListRVAdapter){
        Log.d("loadDiaryList", "loadDiaryList")
        // 검색조건에 따라 쿼리스트링 생성해서 전달
        val query = HashMap<String,String>()
        val archiveListService = ArchiveListService()
        archiveListService.setArchiveListView(this)
        archiveListService.getList(getUserIdx(), pageNum, query, adapter)
    }

    override fun onArchiveListLoading() {
    }

    override fun onArchiveListSuccess(
        result: ArrayList<ArchiveListResult>,
        pageInfo: ArchiveListPageInfo,
        adapter: ArchiveListRVAdapter
    ) {
        if(pageInfo.currentPage != 1){
            adapter.deleteLoading()
        }
        // result를 List RecyclerView에 뿌릴 diaryList로 변환
        val diaryList = arrayListOf<Any>()
        for (item in result){
            val topDate = item.month
            if(topDate != lastDate) {
                lastDate = topDate
                diaryList.add(item.month)
            }
            for (i in 0 until item.diaryList.size){
                diaryList.add(item.diaryList[i])
            }
        }
        diaryList.add(0)
        adapter.apply{
            addDiaryList(diaryList)
            notifyItemRangeInserted((pageInfo.currentPage-1)*(result.size), result.size + 1)
        }
        if (pageInfo.hasNext){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onArchiveListFailure(code: Int) {
        
    }
}