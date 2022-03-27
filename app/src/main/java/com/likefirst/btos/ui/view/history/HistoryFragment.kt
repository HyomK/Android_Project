package com.likefirst.btos.ui.view.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

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
import com.likefirst.btos.domain.ApiResult
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.databinding.FragmentHistoryBinding
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

    private var isLoading = false
    private val isLastLoading = false
    val item = ArrayList<Content?>()
    val senderItem = ArrayList<SenderList?>()

    val historyWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            run {
                val query = s.toString()
                if(query=="") requiredPageNum=1
                run{
                    val request = HistoryRequest(getUserIdx(),1 ,liveSearching.value, query)
                    historyViewModel.setSearchQuery(request)
                    when(liveSearching.value){
                        "sender"->{
                            historyViewModel.postRequest(false,request)
                        }
                        else->{
                            historyViewModel.postRequest(false,request)
                        }
                    }
                }
            }
        }
    }



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
        initAPIObserver()
    }

    fun initAPIObserver(){
        historyViewModel.sender_li.observe(viewLifecycleOwner, Observer<ApiResult<HistoryBaseResponse<BasicHistory<SenderList>>>> {
            when (it.status) {
                ApiResult.Status.SUCCESS -> {
                    setApiSuccessView()
                    Log.e("api-sender-result", it.data?.result?.list.toString())
                    livePageInfo.postValue(it.data?.pageInfo)
                    liveSenderResponse.postValue(it.data?.result?.list)
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
                    Log.e("api-dl-result", requiredPageNum.toString()+ " / "+it.code+it.data?.result?.list.toString())
                    setApiSuccessView()
                    livePageInfo.postValue(it.data?.pageInfo)
                    liveResponse.postValue(it.data?.result?.list)
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


    private fun initObserver(){
        liveResponse.observe(viewLifecycleOwner, Observer {
            run{
                if(historyViewModel.req_flag.value==false){ // init 또는 radio button 눌렀을 때 초기화
                    item.clear()
                    item.addAll(it)
                    recyclerViewAdapter.setdlItems(item)
                    recyclerViewAdapter.notifyDataSetChanged()
                    if (livePageInfo.value?.hasNext!!){
                        requiredPageNum++
                    } else {
                        requiredPageNum = 1
                    }
                }
            }
        })

        liveSenderResponse.observe(viewLifecycleOwner, Observer {
            run{
                if(historyViewModel.req_flag.value==false){
                    senderItem.clear()
                    senderItem.addAll(it)
                    recyclerViewAdapter.setSenderItems(senderItem)
                    recyclerViewAdapter.notifyDataSetChanged()

                    if (livePageInfo.value?.hasNext!!){
                        requiredPageNum++
                    } else {
                        requiredPageNum = 1
                    }
                }
            }
        })
        liveSearching.observe(viewLifecycleOwner, Observer {
            search.setText("")
        })

    }


    private fun init(){
        requiredPageNum = 1
        search=binding.historyToolbar.historySearchEt
        binding.historyToolbar.historySearchEt.addTextChangedListener(historyWatcher)
        userDatabase = UserDatabase.getInstance(requireContext())!!
        binding.historyBasicLoadingPb.bringToFront()
        binding.historyBasicRv.bringToFront()
        initRecyclerView()
        initHistoryList()
        initSearch()
        val request =HistoryRequest(getUserIdx(),1,"sender","")
        historyViewModel.postRequest(false,request)
    }

    //BaseFragment에 OnViewCreated 에서 override되어 자동실행됨 한번더 넣어서 중복 호출되고 있었음
    override fun initAfterBinding() {
        init()
    }

    fun radioButton(id : Int){
        when (id) {
            R.id.history_radiobutton_first->{
                filtering = "sender"
                recyclerViewAdapter.cleardlItems()
            }
            R.id.history_radiobutton_second-> {
                filtering = "diary"
                recyclerViewAdapter.clearSenderItems()
                recyclerViewAdapter.cleardlItems()

            }
            R.id.history_radiobutton_third-> {
                filtering = "letter"
                recyclerViewAdapter.clearSenderItems()
                recyclerViewAdapter.cleardlItems()

            }
        }
        item.clear()
        senderItem.clear()
        recyclerViewAdapter.notifyDataSetChanged()
        liveSearching.postValue(filtering)
        binding.historyBasicNoResultIv.visibility=View.GONE
    }


    private fun initRecyclerView() {
        binding.historyBasicRv.apply {
            adapter = recyclerViewAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        recyclerViewAdapter.setMyItemClickListener(object :
            HistoryBasicRecyclerViewAdapter.MyItemClickListener {
            override fun moveToSenderDetail(sender: String) {

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

    }

    fun initHistoryList(){
        val linearLayoutManager  = binding.historyBasicRv.layoutManager as LinearLayoutManager
        binding.historyBasicRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 스크롤이 끝에 도달했는지 확인
                val lastItem: Int = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading and !isLastLoading and livePageInfo.value?.hasNext!!) {
                    if (linearLayoutManager != null && lastItem == recyclerViewAdapter.itemCount - 1 &&requiredPageNum!=1) {
                        loadHistoryList(requiredPageNum)
                        isLoading = true
                        onLoadMore()
                    }
                }
            }
        })
    }


    fun onLoadMore() {
        val runnable = Runnable {
            when(liveSearching.value){
                "sender"->{
                    senderItem.add(null)
                    recyclerViewAdapter.notifyItemInserted(senderItem.size - 1);
                }
                else->{
                    item.add(null)
                    recyclerViewAdapter.notifyItemInserted(item.size - 1);
                }
            }
        }
        binding.historyBasicRv.post(runnable)

         CoroutineScope(Dispatchers.Main).launch {
            delay(500)
             var scrollPosition=0
             var currentSize = 0
             var nextLimit = currentSize+20
             when(liveSearching.value){
                 "sender"->{
                     if(senderItem.size>1){
                         senderItem.removeAt( senderItem.size-1)
                         scrollPosition = senderItem.size
                         recyclerViewAdapter.notifyItemRemoved(scrollPosition)
                         if(livePageInfo.value!=null){
                             var index =0
                             while (index< livePageInfo.value?.dataNum_currentPage!!){
                                 senderItem.add( liveSenderResponse.value?.get(index))
                                 index++
                             }
                         } // 20개 미만 넣는 경우
                         recyclerViewAdapter.setSenderItems(senderItem!!)
                         recyclerViewAdapter.notifyItemRangeInserted(item.size-livePageInfo.value?.dataNum_currentPage!!, livePageInfo.value?.dataNum_currentPage!!)
                         if (livePageInfo.value?.hasNext!!){
                             requiredPageNum++
                         } else {
                             requiredPageNum = 1
                         }
                     }
                 }
                 else->{
                     if(item.size>1){
                         item.removeAt( item.size-1)
                         scrollPosition = item.size
                         recyclerViewAdapter.notifyItemRemoved( scrollPosition)
                         if(livePageInfo.value!=null){
                             var index =0
                             while (index< livePageInfo.value?.dataNum_currentPage!!){
                                 item.add( liveResponse.value?.get(index))
                                 index++
                             }
                         }
                         recyclerViewAdapter.setdlItems(item!!)
                         recyclerViewAdapter.notifyItemRangeInserted(item.size-livePageInfo.value?.dataNum_currentPage!!, livePageInfo.value?.dataNum_currentPage!!)
                         if (livePageInfo.value?.hasNext!!){
                             requiredPageNum++
                         } else {
                             requiredPageNum = 1
                         }
                     }
                 }
             }
             isLoading = false
        }
    }

    fun loadHistoryList(pageNum : Int){
        val query =binding.historyToolbar.historySearchEt.text.toString()
        val request =HistoryRequest(getUserIdx(),pageNum,liveSearching.value,query)
        historyViewModel.postRequest(true,request)
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

    override fun onPause() {
        super.onPause()
        search.removeTextChangedListener(historyWatcher)
    }

    override fun onStart() {
        super.onStart()
        search.addTextChangedListener(historyWatcher)
    }

}