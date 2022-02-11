package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
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
        var datePickerFlag = true
        var query = HashMap<String, String>()
    }

    override fun initAfterBinding() {
        //companion object 초기화
        requiredPageNum = 1
        lastDate = ""
        datePickerFlag = true
        query.clear()

        val mAdapter = ArchiveListRVAdapter(requireContext())
        initToolbar(mAdapter)
        initDatePicker(mAdapter)
        initDiaryList(mAdapter)
    }

    fun initDatePicker(mAdapter: ArchiveListRVAdapter){
        //datePicker 기능 설정
        binding.archiveListToolbar.archiveListPeriodIv.setOnClickListener {
            if(it.isSelected){
                query.remove("startDate")
                query.remove("endDate")
                reLoadDiaryList(mAdapter, query)
                it.isSelected = !it.isSelected
            } else {
                if(datePickerFlag){
                    val datePickerDialog = ArchiveListPeriodDialog.newInstance()
                    datePickerDialog.setDatePickerClickListener(object : ArchiveListPeriodDialog.DatePickerClickListener{
                        override fun onDatePicked(dateFrom: String, dateTo: String) {
                            query["startDate"] = dateFrom
                            query["endDate"] = dateTo
//                            binding.archiveListRv.clearOnScrollListeners()
//                            mAdapter.clearList()
                            reLoadDiaryList(mAdapter, query)
                            it.isSelected = !it.isSelected
                        }
                    })
                    datePickerDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
                    datePickerDialog.show(childFragmentManager, datePickerDialog.tag)
                    datePickerFlag = false
                }
            }
        }
    }

    fun reLoadDiaryList(mAdapter: ArchiveListRVAdapter, query: HashMap<String, String>){
        binding.archiveListRv.clearOnScrollListeners()
        binding.archiveListRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.archiveListRv.canScrollVertically(1) && !mAdapter.isDiaryEmpty()) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        mAdapter.deleteLoading()
                        mAdapter.notifyItemRemoved(mAdapter.itemCount)
                        binding.archiveListRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        loadDiaryList(requiredPageNum, mAdapter, query)
                    }
                }
            }
        })
        requiredPageNum = 1
        lastDate = ""
        mAdapter.clearList()
        Log.d("requiredPageNum", requiredPageNum.toString())
        loadDiaryList(requiredPageNum, mAdapter, query)
    }

    fun initDiaryList(mAdapter : ArchiveListRVAdapter){
        query.clear()
        val mDecoration = ArchiveListItemDecoration()
        mDecoration.setSize(requireContext())
        binding.archiveListRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.archiveListRv.canScrollVertically(1) && !mAdapter.isDiaryEmpty()) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        mAdapter.deleteLoading()
                        mAdapter.notifyItemRemoved(mAdapter.itemCount)
                        binding.archiveListRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        loadDiaryList(requiredPageNum, mAdapter, query)
                    }
                }
            }
        })
        binding.archiveListRv.apply {
            adapter = mAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            if (itemDecorationCount == 0){
                addItemDecoration(mDecoration)
            }
        }
        loadDiaryList(requiredPageNum, mAdapter, query)
    }

    fun loadDiaryList(pageNum : Int, adapter : ArchiveListRVAdapter, query : HashMap<String,String>){
        // 검색조건에 따라 쿼리스트링 생성해서 전달
        val archiveListService = ArchiveListService()
        archiveListService.setArchiveListView(this)
        archiveListService.getList(getUserIdx(), pageNum, query, adapter)
    }

    fun initToolbar(mAdapter: ArchiveListRVAdapter){
        binding.archiveListToolbar.archiveListSearchIv.setOnClickListener {
            if (binding.archiveListToolbar.archiveListSearchEt.isVisible){
                query["search"] = binding.archiveListToolbar.archiveListSearchEt.text.toString()
                reLoadDiaryList(mAdapter, query)
            } else {
                binding.archiveListToolbar.archiveListSearchEt.visibility = View.VISIBLE
                binding.archiveListToolbar.archiveListBackIv.visibility = View.VISIBLE
            }
        }
        binding.archiveListToolbar.archiveListBackIv.setOnClickListener {
            query.remove("search")
            reLoadDiaryList(mAdapter, query)
            binding.archiveListToolbar.archiveListSearchEt.visibility = View.GONE
            binding.archiveListToolbar.archiveListBackIv.visibility = View.GONE
        }
        binding.archiveListToolbar.archiveListSearchEt.setOnEditorActionListener { textView, actionId, keyEvent ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                query["search"] = textView.text.toString()
                reLoadDiaryList(mAdapter, query)
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }

    override fun onArchiveListLoading() {
        binding.archiveListNoSearchResultLayout.visibility = View.GONE
        binding.archiveListLoadingView.setAnimation("sprout_loading.json")
        binding.archiveListLoadingView.visibility = View.VISIBLE
    }

    override fun onArchiveListSuccess(
        result: ArrayList<ArchiveListResult>,
        pageInfo: ArchiveListPageInfo,
        adapter: ArchiveListRVAdapter
    ) {
        binding.archiveListNoSearchResultLayout.visibility = View.GONE
        binding.archiveListLoadingView.visibility = View.GONE
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
        if (pageInfo.hasNext){
            diaryList.add(0)
        }
        adapter.apply{
            addDiaryList(diaryList)
            notifyItemRangeInserted((pageInfo.currentPage-1)*(result.size), result.size + 1)
        }
        if (pageInfo.hasNext){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
        Log.d("diaryList", diaryList.toString())
    }

    override fun onArchiveListFailure(code: Int) {
        binding.archiveListLoadingView.visibility = View.GONE
        //TODO: 검색결과 없음 화면처리
        when (code){
            6018 -> binding.archiveListNoSearchResultLayout.visibility = View.VISIBLE
        }
    }
}