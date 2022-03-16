package com.likefirst.btos.presentation.View.history

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.SenderDetailView
import com.likefirst.btos.databinding.FragmentHistorySenderDetailBinding
import com.likefirst.btos.presentation.BaseFragment
import com.likefirst.btos.presentation.View.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


class SenderDetailFragment(private val userIdx : Int, private val senderNickName : String)
    : BaseFragment<FragmentHistorySenderDetailBinding>(FragmentHistorySenderDetailBinding::inflate),MainActivity.onBackPressedListener, SenderDetailView{

    companion object {
        var requiredPageNum = 1
    }
    lateinit var toolbar: Toolbar
    lateinit var back : ImageView

    override fun initAfterBinding() {

        binding.historyDetailToolbar.historyBackIv.setOnClickListener{
            if(binding.historyDetailToolbar.historySearchEt.visibility == View.GONE){
                requireActivity().supportFragmentManager.popBackStack()
            }
            else{
                binding.historyDetailToolbar.historySearchEt.visibility = View.GONE
                binding.historyDetailToolbar.historySearchEt.setText("")
            }
        }

        requiredPageNum = 1
        binding.itemHistorySenderTitle.text = senderNickName
        initSenderDetailList()

    }

    private fun initSenderDetailList() {
        val recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireContext(), "senderDetail", userIdx)

        binding.fragmentSenderRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.fragmentSenderRv.canScrollVertically(1)) {
                    if (requiredPageNum == 0) {
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        binding.fragmentSenderRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        Log.e("SenderDETAIL", "else 안")
                        loadSenderDetail(requiredPageNum, recyclerViewAdapter)
                    }
                }
            }
        })
        binding.fragmentSenderRv.apply {
            adapter = recyclerViewAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        recyclerViewAdapter.setMyItemClickListener(object : HistoryBasicRecyclerViewAdapter.MyItemClickListener{
            override fun moveToSenderDetail(userIdx: Int, sender: String) {
            }

            override fun moveToHistoryList(userIdx: Int, type: String, typeIdx: Int) {
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fr_layout,HistoryDetailFragment(userIdx, type, typeIdx), "senderdetail")
//                    .addToBackStack(null)
//                    .commit()
                val intent = Intent(requireActivity(),HistoryDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }

        })
        loadSenderDetail(requiredPageNum, recyclerViewAdapter)
    }

    fun loadSenderDetail(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter){
        val historyService = HistoryService()
        historyService.setSenderDetailView(this)
        historyService.senderDetail(userIdx, senderNickName, pageNum, null, adapter)
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSenderDetailLoading() {
    }

    override fun onSenderDetailSuccess(
        response: ArrayList<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        recyclerViewAdapter.setdlItems(response)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum -1)*20,(requiredPageNum -1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onSenderDetailFailure(code: Int, message: String) {
    }
}