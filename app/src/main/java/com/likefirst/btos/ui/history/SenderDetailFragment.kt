package com.likefirst.btos.ui.history

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.SenderDetailView
import com.likefirst.btos.databinding.FragmentHistorySenderDetailBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class SenderDetailFragment(private val senderNickName : String)
    : BaseFragment<FragmentHistorySenderDetailBinding>(FragmentHistorySenderDetailBinding::inflate),MainActivity.onBackPressedListener, SenderDetailView{

    companion object {
        var requiredPageNum = 1
    }
    var searchText : String ?= null
    lateinit var userDatabase : UserDatabase
    lateinit var recyclerViewAdapter : HistoryBasicRecyclerViewAdapter
    lateinit var searchet : EditText

    val watcher by lazy{
        object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.e("SEARCH",p0.toString())
                searchText = p0.toString()
                loadSenderDetail(1, recyclerViewAdapter)
            }
        }
    }

    override fun initAfterBinding() {
        requiredPageNum = 1
        binding.itemHistorySenderTitle.text = senderNickName
        userDatabase = UserDatabase.getInstance(requireContext())!!
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireContext(), "senderDetail", userDatabase.userDao().getUserIdx())

        binding.historyBasicLoadingPb.bringToFront()

        initSenderDetailList()
        initSearch()
    }

    private fun initSearch() {
        var back : ImageView = binding.historyDetailToolbar.historyBackIv
        searchet = binding.historyDetailToolbar.historySearchEt

        back.setOnClickListener{
            if(searchet.visibility == View.GONE){
                requireActivity().supportFragmentManager.popBackStack()
            }
            else{
                searchet.visibility = View.GONE
                searchet.setText("")
            }
        }

        binding.historyDetailToolbar.historySearchIv.setOnClickListener {
            // 검색 버튼을 누른 경우
            searchet.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
        }

        searchet.addTextChangedListener(watcher)

    }

    private fun initSenderDetailList() {
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
            override fun moveToSenderDetail(sender: String) {
            }

            override fun moveToHistoryList(userIdx: Int, type: String, typeIdx: Int) {
                val intent = Intent(requireActivity(),HistoryDetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.apply {
                    this.putExtra("userIdx",userIdx)
                    this.putExtra("type",type)
                    this.putExtra("typeIdx",typeIdx)
                }
                Log.e("SENDERDETAIL", userIdx.toString() + type + typeIdx.toString())
                startActivity(intent)
            }

        })
        loadSenderDetail(requiredPageNum, recyclerViewAdapter)
    }

    fun loadSenderDetail(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter){
        val historyService = HistoryService()
        historyService.setSenderDetailView(this)
        historyService.senderDetail(userDatabase.userDao().getUserIdx(), senderNickName, pageNum, searchText, adapter)
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSenderDetailLoading() {
        binding.historyBasicLoadingPb.visibility = View.VISIBLE
        binding.historyBasicLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onSenderDetailSuccess(
        response: ArrayList<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
        if(searchText!=null) {
            recyclerViewAdapter.cleardlItems()
            recyclerViewAdapter.notifyDataSetChanged()
        }
        recyclerViewAdapter.setdlItems(response)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum -1)*20,(requiredPageNum -1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onSenderDetailFailure(code: Int, message: String) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        when(code){
            6018 ->{
                //검색결과 없음
                recyclerViewAdapter.cleardlItems()
                recyclerViewAdapter.notifyDataSetChanged()
                binding.historyBasicNoResultIv.visibility = View.VISIBLE
                binding.historyBasicNoResultTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        searchet.removeTextChangedListener(watcher)
    }
}