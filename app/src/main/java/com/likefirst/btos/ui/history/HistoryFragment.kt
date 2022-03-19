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
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.HistoryDiaryandLetterView
import com.likefirst.btos.data.remote.history.view.HistorySenderView
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate)
    , HistorySenderView, HistoryDiaryandLetterView {

    var requiredPageNum = 1
    var filtering : String = "sender"
    lateinit var userDatabase : UserDatabase
    lateinit var recyclerViewAdapter : HistoryBasicRecyclerViewAdapter
    lateinit var search : EditText
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
                initRecyclerView()
            }
        }
    }

    override fun initAfterBinding() {
        userDatabase = UserDatabase.getInstance(requireContext())!!
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(), filtering, userDatabase.userDao().getUserIdx())
        search = binding.historyToolbar.historySearchEt
        binding.historyBasicLoadingPb.bringToFront()
        binding.historyBasicRv.bringToFront()

        initRecyclerView()
        initSearch()
    }

    fun radioButton(id : Int){
        when (id) {
            R.id.history_radiobutton_first->{
                filtering = "sender"
            }
            R.id.history_radiobutton_second-> {
                filtering = "diary"
            }
            R.id.history_radiobutton_third-> {
                filtering = "letter"
            }
        }
        requiredPageNum = 1
        Log.e("RADIOBUTTON",filtering)
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(), filtering, userDatabase.userDao().getUserIdx())
        initRecyclerView()
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
                Log.e("HISTORY",
                    userDatabase.userDao().getUserIdx().toString() + pageNum.toString() + filtering)
                historyService.sender(userDatabase.userDao().getUserIdx(), pageNum, filtering, searchText, adapter)
            }
            else ->{
                historyService.sethistoryDiaryView(this)
                Log.e("HISTORY",
                    userDatabase.userDao().getUserIdx().toString() + pageNum.toString() + filtering)
                historyService.diaryletter(userDatabase.userDao().getUserIdx(), pageNum, filtering, searchText, adapter)
            }
        }
    }

    private fun initSearch() {
        val mActivity = activity as MainActivity
        val back : ImageView = binding.historyToolbar.historyBackIv
        val searchiv = binding.historyToolbar.historySearchIv

        searchiv.setOnClickListener {
            // 검색 버튼을 누른 경우
            search.visibility = View.VISIBLE
            searchiv.visibility = View.VISIBLE
            if(search.text.toString() != "") {
                search.setText("")
            }
        }
        back.setOnClickListener {
            checkET()
            checkView()
            mActivity.hideKeyboard(requireView())
        }
        binding.historyRadiogroup.setOnCheckedChangeListener { radioGroup, i ->
            checkView()
            mActivity.hideKeyboard(requireView())
            radioButton(i)
        }
        search.addTextChangedListener(watcher)
    }


    fun checkET(){
        if(binding.historyToolbar.historySearchEt.visibility == View.VISIBLE && binding.historyToolbar.historySearchEt.text.toString() != "") {
            binding.historyToolbar.historySearchEt.setText("")
        }
    }

    fun checkView(){
        binding.historyToolbar.historySearchEt.visibility = View.GONE
        binding.historyToolbar.historyBackIv.visibility = View.GONE
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
        if(searchText!=null) {
            recyclerViewAdapter.clearSenderItems()
            recyclerViewAdapter.notifyDataSetChanged()
        }
        recyclerViewAdapter.setSenderItems(result)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
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
                recyclerViewAdapter.notifyDataSetChanged()
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
        if(searchText!=null) {
            recyclerViewAdapter.cleardlItems()
            recyclerViewAdapter.notifyDataSetChanged()
        }
        recyclerViewAdapter.setdlItems(result)
        Log.e("HISTORYRECYCLER","required"+requiredPageNum+"current"+pageInfo.dataNum_currentPage)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
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
                recyclerViewAdapter.notifyDataSetChanged()
                binding.historyBasicNoResultIv.visibility = View.VISIBLE
                binding.historyBasicNoResultTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            checkView()
        }else search.removeTextChangedListener(watcher)
    }

}

//package com.likefirst.btos.ui.history
//
//import android.os.Bundle
//import android.view.View
//import com.likefirst.btos.R
//import com.likefirst.btos.databinding.FragmentHistoryBinding
//import com.likefirst.btos.ui.BaseFragment
//import com.likefirst.btos.ui.main.MainActivity
//
//class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
//
//    override fun initAfterBinding() {
//        val mActivity = activity as MainActivity
//
//        binding.historyToolbar.historySearchIv.setOnClickListener {
//            // 검색 버튼을 누른 경우
//            binding.historyToolbar.historySearchEt.visibility = View.VISIBLE
//            binding.historyToolbar.historyBackIv.visibility = View.VISIBLE
//            if(binding.historyToolbar.historySearchEt.text.toString() != "") {
//                binding.historyToolbar.historySearchEt.setText("")
//            }
//        }
//        binding.historyToolbar.historyBackIv.setOnClickListener {
//            checkET()
//            checkView()
//            mActivity.hideKeyboard(requireView())
//        }
//        binding.historyRadiogroup.setOnCheckedChangeListener { radioGroup, i ->
//            checkView()
//            mActivity.hideKeyboard(requireView())
//            radioButton(i)
//        }
//    }
//
//    fun radioButton(id : Int){
//        when (id) {
//            R.id.history_radiobutton_first->{
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.history_fragment,
//                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","sender") } },
//                        "historysender")
//                    .commit()
//            }
//            R.id.history_radiobutton_second-> {
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.history_fragment,
//                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","diary") } },
//                        "historydiary")
//                    .commit()
//            }
//            R.id.history_radiobutton_third-> {
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.history_fragment,
//                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","letter") } },
//                        "historymail")
//                    .commit()
//            }
//        }
//    }
//
//    fun checkET(){
//        if(binding.historyToolbar.historySearchEt.visibility == View.VISIBLE && binding.historyToolbar.historySearchEt.text.toString() != "") {
//            binding.historyToolbar.historySearchEt.setText("")
//        }
//    }
//
//    fun checkView(){
//        binding.historyToolbar.historySearchEt.visibility = View.GONE
//        binding.historyToolbar.historyBackIv.visibility = View.GONE
//    }
//
//    override fun onStart() {
//        super.onStart()
//        radioButton(binding.historyRadiogroup.checkedRadioButtonId)
//    }
//
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if(!hidden){
//            checkView()
//        }
//    }
//}
