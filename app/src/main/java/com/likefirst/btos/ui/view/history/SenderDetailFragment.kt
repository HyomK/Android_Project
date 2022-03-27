package com.likefirst.btos.ui.view.history

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.SenderDetailView
import com.likefirst.btos.databinding.FragmentHistorySenderDetailBinding
import com.likefirst.btos.domain.ApiResult
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.ui.viewModel.HistoryRequest
import com.likefirst.btos.ui.viewModel.HistorySearchViewModel
import com.likefirst.btos.utils.getUserIdx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SenderDetailFragment(private val senderNickName : String)
    : BaseFragment<FragmentHistorySenderDetailBinding>(FragmentHistorySenderDetailBinding::inflate),
    MainActivity.onBackPressedListener, SenderDetailView{

    companion object {
        var requiredPageNum = 1
    }
    var searchText : String ?= null
    lateinit var userDatabase : UserDatabase
    lateinit var recyclerViewAdapter : HistoryBasicRecyclerViewAdapter


    lateinit var liveResponse: MutableLiveData<ArrayList<Content>>
    var liveSearching = MutableLiveData<String>("senderDetail")
    var livePageInfo = MutableLiveData<PageInfo>()


    val historyViewModel  by viewModels<HistorySearchViewModel>()

    private var isLoading = false
    private val isLastLoading = false
    val item = ArrayList<Content?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("move to", "senderDetail")
        liveResponse = MutableLiveData()
        liveSearching = MutableLiveData()
        recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(),getUserIdx(),liveSearching)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }


    override fun initAfterBinding() {
        requiredPageNum = 1
        binding.itemHistorySenderTitle.text = senderNickName
        userDatabase = UserDatabase.getInstance(requireContext())!!
        binding.historyBasicLoadingPb.bringToFront()
        initSearch()
        initSenderDetailList()
        historyViewModel.getSenderDetailList(false, getUserIdx(), senderNickName,requiredPageNum,"")
    }



    private fun initObserver(){
        historyViewModel.sender_detail_res.observe(viewLifecycleOwner, Observer<ApiResult<HistorySenderDetailResponse>> {
            when (it.status) {
                ApiResult.Status.SUCCESS -> {
                    setApiSuccessView()
                    livePageInfo.postValue(it.data?.pageInfo)
                    liveResponse.postValue(it.data?.result)

                }
                ApiResult.Status.LOADING -> {
                    setLoadingView()
                }
                ApiResult.Status.API_ERROR -> {
                    setApiFailView()
                }
            }
        })

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
    }



    private fun initSearch() {
        var back: ImageView = binding.historyDetailToolbar.historyBackIv
        binding.historyDetailToolbar.historySearchEt.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                run {
                    val query = s.toString()
                    if(query=="") requiredPageNum=1
                    historyViewModel.getSenderDetailList(false, getUserIdx(), senderNickName,1,query)
                }
            }
        })

        back.setOnClickListener {
            if ( binding.historyDetailToolbar.historySearchEt.visibility == View.GONE) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                binding.historyDetailToolbar.historySearchEt.visibility = View.GONE
                binding.historyDetailToolbar.historySearchEt.setText("")
            }
        }

        binding.historyDetailToolbar.historySearchIv.setOnClickListener {
            // 검색 버튼을 누른 경우
            binding.historyDetailToolbar.historySearchEt.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
        }



        binding.fragmentSenderRv.apply {
            adapter = recyclerViewAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        recyclerViewAdapter.setMyItemClickListener(object :
            HistoryBasicRecyclerViewAdapter.MyItemClickListener {
            override fun moveToSenderDetail(sender: String) {
            }

            override fun moveToHistoryList(userIdx: Int, type: String, typeIdx: Int) {
                val intent = Intent(requireActivity(), HistoryDetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.apply {
                    this.putExtra("userIdx", userIdx)
                    this.putExtra("type", type)
                    this.putExtra("typeIdx", typeIdx)
                }
                Log.e("SENDERDETAIL", userIdx.toString() + type + typeIdx.toString())
                startActivity(intent)
            }

        })

        binding.fragmentSenderRv.adapter=recyclerViewAdapter

    }

   fun initSenderDetailList() {
        val query = binding.historyDetailToolbar.historySearchEt.text.toString()
        val linearLayoutManager  = binding.fragmentSenderRv.layoutManager as LinearLayoutManager
        binding.fragmentSenderRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem: Int = linearLayoutManager.findLastVisibleItemPosition()

                if (!isLoading and !isLastLoading  and livePageInfo.value?.hasNext!!) {
                    if (linearLayoutManager != null && lastItem == recyclerViewAdapter.itemCount - 1 && requiredPageNum!=1) {
                        historyViewModel.getSenderDetailList(true, getUserIdx(),senderNickName , requiredPageNum,query)
                        isLoading = true
                        onLoadMore()
                    }
                }
            }
        })
    }

     fun onLoadMore() {
         val runnable = Runnable {
             item.add(null)
             recyclerViewAdapter.notifyItemInserted(item.size - 1);
         }
         binding.fragmentSenderRv.post(runnable)

         CoroutineScope(Dispatchers.Main).launch {
             delay(500)
             var scrollPosition=0

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
             isLoading = false
         }
     }

    override fun onBackPressed() {
        //requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    fun setLoadingView(){
        binding.historyBasicLoadingPb.visibility = View.VISIBLE
        binding.historyBasicLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    fun setApiSuccessView(){
        binding.historyBasicLoadingPb.visibility = View.GONE
        binding.historyBasicNoResultIv.visibility = View.GONE
        binding.historyBasicNoResultTv.visibility = View.GONE

    }


    fun setApiFailView(){
        binding.historyBasicLoadingPb.visibility = View.GONE
        recyclerViewAdapter.cleardlItems()
        recyclerViewAdapter.notifyDataSetChanged()
        binding.historyBasicNoResultIv.visibility = View.VISIBLE
        binding.historyBasicNoResultTv.visibility = View.VISIBLE
    }


    override fun onSenderDetailLoading() {

    }

    override fun onSenderDetailSuccess(
        response: ArrayList<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {

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


}