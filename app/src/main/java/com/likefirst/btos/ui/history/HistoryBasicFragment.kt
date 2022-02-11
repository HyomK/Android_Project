
package com.likefirst.btos.ui.history

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.HistoryDiaryandLetterView
import com.likefirst.btos.data.remote.history.view.HistorySenderView
import com.likefirst.btos.databinding.FragmentHistoryBasicBinding
import com.likefirst.btos.ui.BaseFragment

class HistoryBasicFragment(val filtering: String) : BaseFragment<FragmentHistoryBasicBinding>(FragmentHistoryBasicBinding::inflate)
,HistorySenderView, HistoryDiaryandLetterView{

    companion object{
        var requiredPageNum = 1
    }

    override fun initAfterBinding() {
        requiredPageNum = 1
        initHistoryList()
    }

    fun initHistoryList(){
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        val recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireContext(), filtering, userDatabase.userDao().getUserIdx())

        binding.historyBasicRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.historyBasicRv.canScrollVertically(1)) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        binding.historyBasicRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        Log.e("HISTORYBASIC","else 안")
                        loadHistoryList(requiredPageNum, recyclerViewAdapter)
                    }
                }
            }
        })
        binding.historyBasicRv.apply {
            adapter = recyclerViewAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        recyclerViewAdapter.setMyItemClickListener(object : HistoryBasicRecyclerViewAdapter.MyItemClickListener{
            override fun moveToSenderDetail(userIdx: Int, sender: String) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fr_layout,SenderDetailFragment(userIdx, sender), "senderdetail")
                    .addToBackStack(null)
                    .commit()
            }

            override fun moveToHistoryList(userIdx: Int, type: String, typeIdx: Int) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fr_layout,HistoryDetailFragment(userIdx, type, typeIdx), "senderdetail")
                    .addToBackStack(null)
                    .commit()
            }
        })
        loadHistoryList(requiredPageNum, recyclerViewAdapter)
    }

    fun loadHistoryList(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter){
        val historyService = HistoryService()
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        when(filtering){
            "sender"->{
                historyService.setHistorySenderView(this)
                Log.e("HISTORYBASIC",userDatabase.userDao().getUserIdx().toString()+ pageNum.toString()+filtering,)
                historyService.sender(userDatabase.userDao().getUserIdx(), pageNum, filtering,null, adapter)
            }
            else ->{
                historyService.sethistoryDiaryView(this)
                Log.e("HISTORYBASIC",userDatabase.userDao().getUserIdx().toString()+ pageNum.toString()+filtering,)
                historyService.diaryletter(userDatabase.userDao().getUserIdx(), pageNum, filtering,null, adapter)
            }
        }
    }

    override fun onHistorySenderLoading() {
    }

    override fun onHistorySenderSuccess(response: BasicHistory<SenderList>, pageInfo: PageInfo, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter) {
        val result = response.list
        recyclerViewAdapter.setSenderItems(result)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }
    override fun onHistorySenderFailure(code: Int, message: String) {
    }

    override fun onHistoryDiaryLoading() {

    }

    override fun onHistoryDiarySuccess(
        response: BasicHistory<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        val result = response.list
        recyclerViewAdapter.setdlItems(result)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onHistoryDiaryFailure(code: Int, message: String) {

    }

}

