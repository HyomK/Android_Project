package com.likefirst.btos.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.domain.ApiResult
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.ui.view.history.HistoryAdapter
import com.likefirst.btos.ui.view.history.HistoryBasicRecyclerViewAdapter
import com.likefirst.btos.ui.view.history.HistoryDetailActivity
import com.likefirst.btos.ui.view.history.SenderDetailFragment
import com.likefirst.btos.ui.viewModel.HistorySearchViewModel
import com.likefirst.btos.ui.viewModel.HistoryRequest
import com.likefirst.btos.utils.getUserIdx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    var requiredPageNum = 1
    var filtering : String = "sender"
    lateinit var userDatabase : UserDatabase
    lateinit var recyclerViewAdapter : HistoryBasicRecyclerViewAdapter
    lateinit var search : EditText

    val historyViewModel  by viewModels<HistorySearchViewModel>()

    lateinit var liveResponse: MutableLiveData<ArrayList<Content>>
    lateinit  var liveSenderResponse  : MutableLiveData<ArrayList<SenderList>>
    var liveSearching = MutableLiveData<String>(filtering)
    var livePageInfo = MutableLiveData<PageInfo>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        liveResponse = MutableLiveData()
        liveSenderResponse = MutableLiveData()
        liveSearching = MutableLiveData()
        liveSearching.postValue("sender")
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(),getUserIdx(),liveSearching)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        init()
        initAfterBinding()
        liveResponse.observe(viewLifecycleOwner, Observer {
            run{
                binding.historyBasicRv.visibility=View.VISIBLE
                recyclerViewAdapter.setdlItems(it)
                if(livePageInfo.value != null) recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+livePageInfo.value!!.dataNum_currentPage!!)
                requiredPageNum = 1

            }
        })

        liveSenderResponse.observe(viewLifecycleOwner, Observer {
            run{
                binding.historyBasicRv.visibility=View.VISIBLE
                recyclerViewAdapter.setSenderItems(it)
                if(livePageInfo.value != null) recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+livePageInfo.value!!.dataNum_currentPage!!)
                requiredPageNum = 1

            }
        })
    }


    private fun initObserver(){
        historyViewModel.sender_li.observe(viewLifecycleOwner, Observer<ApiResult<HistoryBaseResponse<BasicHistory<SenderList>>>> {
            when (it.status) {
                ApiResult.Status.SUCCESS -> {
                    setApiSuccessView()
                    Log.e("WATCHRMODEL-result", it.data?.result?.list.toString())
                    if(it.data?.result !=null){
                        liveSenderResponse.postValue(it.data?.result?.list)
                        livePageInfo.postValue(it.data?.pageInfo)
                        if (it.data?.pageInfo?.hasNext!!){
                            requiredPageNum++
                        } else {
                            requiredPageNum = 0
                        }
                    }
                }
                ApiResult.Status.LOADING -> {
                     setLoadingView()
                }
                ApiResult.Status.API_ERROR -> {
                    setApiFailView()
                }
            }
        })

        historyViewModel.dl_li.observe(viewLifecycleOwner, Observer<ApiResult<HistoryBaseResponse<BasicHistory<Content>>>> {
            when (it.status) {
                ApiResult.Status.SUCCESS -> {
                    Log.e("WATCHRMODEL-result", it.code+it.data?.result?.list.toString())
                    setApiSuccessView()

                    if(it.data?.result !=null){
                        liveResponse.postValue(it.data?.result?.list)
                        livePageInfo.postValue(it.data?.pageInfo)
                        if (it.data?.pageInfo?.hasNext!!){
                            requiredPageNum++
                        } else {
                            requiredPageNum = 0
                        }
                    }
                }
                ApiResult.Status.LOADING -> {
                    setLoadingView()
                }
                ApiResult.Status.API_ERROR -> {
                    setApiFailView()
                }
            }
        })
    }

    private fun init(){
        binding.historyBasicRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        requiredPageNum = 1
        search=binding.historyToolbar.historySearchEt
        binding.historyToolbar.historySearchEt.doOnTextChanged { text, start, before, count ->
           run{
               val query = text.toString()
               Log.e("WATCHRMODEL", query.toString()+"/"+liveSearching.value.toString())
               val request = HistoryRequest(getUserIdx(),requiredPageNum ,liveSearching.value, query)
               historyViewModel.setSearchQuery(request)
               when(liveSearching.value){
                   "sender"->{
                       //pageNum
                       /*lifecycleScope.launchWhenStarted {
                           historyViewModel.searchResult.collect { it -> Log.e("WATCHER-SEARCH",it.toString() )}
                       }*/
                       historyViewModel.getSenderList(request)
                   }
                   else->{
                       historyViewModel.getDiaryLetterList(request)
                   }
               }
           }
        }

        liveSearching.observe(viewLifecycleOwner, Observer {
            Log.e("search-filter",it)
            val request = HistoryRequest(getUserIdx(),requiredPageNum ,it, "")
            when(it){
              "sender"->historyViewModel.getSenderList(request)
               else -> historyViewModel.getDiaryLetterList(request)
            }
        })
//        val request =HistoryRequest(getUserIdx(),requiredPageNum,"sender",null)
//        historyViewModel.getSenderList(request)
    }



    override fun initAfterBinding() {

        userDatabase = UserDatabase.getInstance(requireContext())!!
        binding.historyBasicLoadingPb.bringToFront()
        binding.historyBasicRv.bringToFront()
        initRecyclerView()
        initHistoryList()
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
        liveSearching.postValue(filtering)
        binding.historyBasicNoResultIv.visibility=View.GONE


    }


    private fun initRecyclerView() {
        recyclerViewAdapter.setMyItemClickListener(object :
            HistoryBasicRecyclerViewAdapter.MyItemClickListener {
            override fun moveToSenderDetail(sender: String) {
                binding.historyToolbar.historySearchEt.setText("")
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fr_layout,
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

        binding.historyBasicRv.adapter=recyclerViewAdapter
        binding.historyBasicRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    fun initHistoryList(){
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

    }

    fun loadHistoryList(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter){
        val query =binding.historyToolbar.historySearchEt.text.toString()
        when(liveSearching.value){
            "sender"->{
                historyViewModel.getSenderList(HistoryRequest(getUserIdx(),pageNum,liveSearching.value,query))
            }
            else ->{
                historyViewModel.getDiaryLetterList(HistoryRequest(getUserIdx(),pageNum,liveSearching.value,query))
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
            back.visibility = View.VISIBLE
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



    fun setApiFailView(){
        binding.historyBasicLoadingPb.visibility = View.GONE
        for (i in 0 until  binding.historyRadiogroup.childCount) {
            binding.historyRadiogroup.getChildAt(i).isEnabled=true
        }
        recyclerViewAdapter.clearSenderItems()
        recyclerViewAdapter.cleardlItems()
        recyclerViewAdapter.notifyDataSetChanged()
        binding.historyBasicNoResultIv.visibility = View.VISIBLE
    }

    fun setApiSuccessView(){
        binding.historyBasicLoadingPb.visibility = View.GONE
        for (i in 0 until  binding.historyRadiogroup.childCount) {
            binding.historyRadiogroup.getChildAt(i).isEnabled=true
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
        binding.historyBasicRv.visibility = View.VISIBLE
    }

    fun setLoadingView(){
        binding.historyBasicLoadingPb.visibility = View.VISIBLE
        for (i in 0 until  binding.historyRadiogroup.childCount) {
            binding.historyRadiogroup.getChildAt(i).isEnabled=false
        }
        binding.historyBasicLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden ){
            checkView()
        }else if(hidden &&isAdded){
            binding.historyToolbar.historySearchEt.setText("")
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager.findFragmentByTag("senderdetail")?.let { remove(it) }
            }
        }
    }

}