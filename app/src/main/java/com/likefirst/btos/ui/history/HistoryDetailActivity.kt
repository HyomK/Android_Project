package com.likefirst.btos.ui.history

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.HistoryList
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.HistoryDetailView
import com.likefirst.btos.databinding.ActivityHistoryListBinding
import com.likefirst.btos.ui.BaseActivity

class HistoryDetailActivity: BaseActivity<ActivityHistoryListBinding>(ActivityHistoryListBinding::inflate)
,HistoryDetailView{

    val positioning : Int = -1
    lateinit var recyclerView: RecyclerView

    override fun initAfterBinding() {
        recyclerView = binding.historyDetailRv
        binding.historyToolbar.historyDetailBackIv.setOnClickListener{
            finish()
        }

        initHistoryDetailList()
    }

    private fun initHistoryDetailList() {
        val recyclerViewAdapter = HistoryDetailRecyclerViewAdapter(this, resources.getStringArray(
            R.array.emotionNames), recyclerView)
        binding.historyDetailRv.apply {
            adapter = recyclerViewAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        var userIdx = intent.getIntExtra("userIdx",0)
        var type = intent.getStringExtra("type").toString()
        var typeIdx = intent.getIntExtra("typeIdx",0)
        Log.e("HISTORYDETAIL",userIdx.toString()+type+typeIdx.toString())
        val historyService = HistoryService()
        historyService.setHistoryDetailView(this)
//        historyService.historyDetail(userIdx.toInt(), type, typeIdx.toInt(), recyclerViewAdapter)
        historyService.historyDetail(userIdx, type, typeIdx, recyclerViewAdapter)

//        val bundle = intent.getBundleExtra("historyInfo")
//        userIdx = bundle?.getString("userIdx").toString()
//        type = bundle?.getString("type").toString()
//        typeIdx = bundle?.getString("typeIdx").toString()

    }

    override fun onHistoryDetailLoading() {

    }

    override fun onHistoryDetailSuccess(
        response: ArrayList<HistoryList>,
        recyclerViewAdapter: HistoryDetailRecyclerViewAdapter,
    ) {
        recyclerViewAdapter.setHistoryItems(response)
        recyclerViewAdapter.notifyDataSetChanged()
        val size = recyclerViewAdapter.itemCount
        var count = 0
        response.forEach {
            if (it.positioning) {
                recyclerView.scrollToPosition(count)
            }
            count++
        }
    }

    override fun onHistoryDetailFailure(code: Int, message: String) {

    }
}