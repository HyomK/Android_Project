
package com.likefirst.btos.ui.view.history

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
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


class HistoryBasicFragment() : BaseFragment<FragmentHistoryBasicBinding>(FragmentHistoryBasicBinding::inflate)
,HistorySenderView, HistoryDiaryandLetterView{

    var requiredPageNum = 1
    val filtering : String by lazy{
        arguments?.getString("filtering").toString()
    }
    lateinit var userDatabase : UserDatabase
    lateinit var recyclerViewAdapter : HistoryBasicRecyclerViewAdapter
    lateinit var toolbar : Toolbar
    lateinit var search : EditText
    var liveSearching = MutableLiveData<String>()

    var searchText : String ?= null
    val watcher by lazy{
        object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.e("SEARCH",p0.toString())
                searchText = p0.toString()
                loadHistoryList(1, recyclerViewAdapter)
            }
        }
    }

    override fun initAfterBinding() {
        userDatabase = UserDatabase.getInstance(requireContext())!!
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(),  userDatabase.userDao().getUserIdx(),liveSearching)
        toolbar = requireActivity().findViewById(R.id.history_toolbar)
        search = toolbar.findViewById(R.id.history_search_et)

        binding.historyBasicLoadingPb.bringToFront()

        initRecyclerView()
        initSearch()
    }

    private fun initSearch() {
        search.addTextChangedListener(watcher)
    }

    private fun initRecyclerView() {
        val recyclerview : RecyclerView = binding.historyBasicRv

        recyclerview.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            recyclerViewAdapter.setMyItemClickListener(object :
                HistoryBasicRecyclerViewAdapter.MyItemClickListener {
                override fun moveToSenderDetail(sender: String) {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fr_layout,
                            SenderDetailFragment(sender),
                            "senderdetail")
                        .addToBackStack(null)
                        .commit()
                }

                override fun moveToHistoryList(userIdx: Int, type: String, typeIdx: Int) {
                    val intent = Intent(requireContext(), HistoryDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.apply {
                        this.putExtra("userIdx",userIdx)
                        this.putExtra("type",type)
                        this.putExtra("typeIdx",typeIdx)
                    }
                    Log.e("HISTORYBASIC", userIdx.toString() + type + typeIdx.toString())
                    startActivity(intent)
                }
            })

        }

        requiredPageNum = 1
        initHistoryList(recyclerViewAdapter)
    }

    fun initHistoryList(recyclerViewAdapter : HistoryBasicRecyclerViewAdapter){
        binding.historyBasicRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var isEmpty = true
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.historyBasicRv.canScrollVertically(1)) {

                    isEmpty = if(filtering == "sender") recyclerViewAdapter.isSenderEmpty()
                    else recyclerViewAdapter.isDLEmpty()

                    if(!isEmpty){
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
            }
        })


        loadHistoryList(requiredPageNum, recyclerViewAdapter)
    }

    fun loadHistoryList(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter){
        val historyService = HistoryService()
        when(filtering){
            "sender"->{
                historyService.setHistorySenderView(this)
                Log.e("HISTORYBASIC",
                    userDatabase.userDao().getUserIdx().toString() + pageNum.toString() + filtering)
                historyService.sender(userDatabase.userDao().getUserIdx(), pageNum, filtering, searchText, adapter)
            }
            else ->{
                historyService.sethistoryDiaryView(this)
                Log.e("HISTORYBASIC",
                    userDatabase.userDao().getUserIdx().toString() + pageNum.toString() + filtering)
                historyService.diaryletter(userDatabase.userDao().getUserIdx(), pageNum, filtering, searchText, adapter)
            }
        }
    }

    override fun onHistorySenderLoading() {
        binding.historyBasicLoadingPb.visibility = View.VISIBLE
        binding.historyBasicLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onHistorySenderSuccess(response: BasicHistory<SenderList>, pageInfo: PageInfo, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
        val result = response.list

        recyclerViewAdapter.setSenderItems(result)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }
    override fun onHistorySenderFailure(code: Int, message: String, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        when(code){
            6018 ->{
                //검색결과 없음
                recyclerViewAdapter.clearSenderItems()
                binding.historyBasicNoResultIv.visibility = View.VISIBLE
                binding.historyBasicNoResultTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onHistoryDiaryLoading() {
        binding.historyBasicLoadingPb.visibility = View.VISIBLE
        binding.historyBasicLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onHistoryDiarySuccess(
        response: BasicHistory<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
        val result = response.list
       /* if(searchText!=null) {
          recyclerViewAdapter.cleardlItems()
        }*/
        recyclerViewAdapter.setdlItems(result)
      //  recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onHistoryDiaryFailure(code: Int, message: String, recyclerViewAdapter: HistoryBasicRecyclerViewAdapter) {
        binding.historyBasicLoadingPb.visibility = View.GONE
        when(code){
            6018 ->{
                //검색결과 없음
                recyclerViewAdapter.cleardlItems()
                //recyclerViewAdapter.notifyDataSetChanged()
                binding.historyBasicNoResultIv.visibility = View.VISIBLE
                binding.historyBasicNoResultTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        search.removeTextChangedListener(watcher)
    }

}

