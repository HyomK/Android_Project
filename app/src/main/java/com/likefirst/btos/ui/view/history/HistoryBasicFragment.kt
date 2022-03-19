
package com.likefirst.btos.ui.view.history

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
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
import io.reactivex.Observable
import io.reactivex.disposables.Disposable


class HistoryBasicFragment() : BaseFragment<FragmentHistoryBasicBinding>(FragmentHistoryBasicBinding::inflate)
,HistorySenderView, HistoryDiaryandLetterView{

    var requiredPageNum = 1
    var searchText : String? = null
    var disposable: Disposable? = null
    var observeSearchText : Observable<String>? = null
    val filtering : String by lazy{
        arguments?.getString("filtering").toString()
    }

    override fun initAfterBinding() {

        Log.e("filtering",filtering)
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        val recyclerViewAdapter = HistoryBasicRecyclerViewAdapter(requireActivity(), filtering, userDatabase.userDao().getUserIdx())
        val recyclerview : RecyclerView = binding.historyBasicRv
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.history_toolbar)
        val search = toolbar.findViewById<EditText>(R.id.history_search_et)

        recyclerview.apply {
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
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fr_layout,HistoryDetailFragment(userIdx, type, typeIdx), "senderdetail")
//                    .addToBackStack(null)
//                    .commit()
                val intent = Intent(requireContext(),HistoryDetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("userIdx",userIdx)
                intent.putExtra("type",type)
                intent.putExtra("typeIdx",typeIdx)
                Log.e("HISTORYBASIC",userIdx.toString()+type+typeIdx.toString())
//                val bundle = Bundle()
//                bundle.putString("userIdx",userIdx.toString())
//                bundle.putString("type",type)
//                bundle.putString("typeIdx",typeIdx.toString())
//                intent.putExtra("historyInfo",bundle)
                startActivity(intent)
            }
        })

        requiredPageNum = 1
        initHistoryList(recyclerViewAdapter, userDatabase)

        //edittext observer
//        observeSearchText = Observable
//            .create(ObservableOnSubscribe { emitter: ObservableEmitter<String> ->
//                search.addTextChangedListener(object : TextWatcher {
//                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    }
//
//                    override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
//                        Log.e("TEXT밖",text.toString())
//                        if(text != ""){
//                            searchText = text.toString()
//                            Log.e("SEARCH", searchText!!)
//                            emitter.onNext(searchText.toString())
//                        }
//                    }
//
//                    override fun afterTextChanged(p0: Editable?) {
//                    }
//                })
//            })
//            .debounce(400, TimeUnit.MILLISECONDS)
//            .subscribeOn(Schedulers.io())
//
//        observeSearchText?.subscribe(object : Observer<String> {
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//            override fun onNext(t: String) {
//                loadHistoryList(1, recyclerViewAdapter, userDatabase)
//            }
//            override fun onError(e: Throwable) {
//            }
//            override fun onComplete() {
//            }
//        })
    }

    fun initHistoryList(recyclerViewAdapter : HistoryBasicRecyclerViewAdapter, userDatabase: UserDatabase){
        binding.historyBasicRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var isEmpty = true
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.historyBasicRv.canScrollVertically(1)) {

                    if(filtering == "sender") isEmpty = recyclerViewAdapter.isSenderEmpty()
                    else isEmpty = recyclerViewAdapter.isDLEmpty()

                    if(!isEmpty){
                        if(requiredPageNum == 0){
                            // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                            binding.historyBasicRv.clearOnScrollListeners()
                        } else {
                            // 다음 페이지 불러오기
                            Log.e("HISTORYBASIC","else 안")
                            loadHistoryList(requiredPageNum, recyclerViewAdapter, userDatabase)
                        }
                    }
                }
            }
        })

        loadHistoryList(requiredPageNum, recyclerViewAdapter, userDatabase)
    }

    fun loadHistoryList(pageNum : Int, adapter : HistoryBasicRecyclerViewAdapter, userDatabase : UserDatabase){
        val historyService = HistoryService()
//        val userDatabase = UserDatabase.getInstance(requireActivity())!!
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
//        binding.historyBasicLoadingPb.visibility = View.VISIBLE
//        binding.historyBasicNoResultIv.visibility = View.GONE
//        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onHistorySenderSuccess(response: BasicHistory<SenderList>, pageInfo: PageInfo, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter) {
//        binding.historyBasicLoadingPb.visibility = View.GONE
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
//        binding.historyBasicLoadingPb.visibility = View.GONE
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
//        binding.historyBasicLoadingPb.visibility = View.VISIBLE
//        binding.historyBasicNoResultIv.visibility = View.GONE
//        binding.historyBasicNoResultTv.visibility = View.GONE
    }

    override fun onHistoryDiarySuccess(
        response: BasicHistory<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
//        binding.historyBasicLoadingPb.visibility = View.GONE
//        binding.historyBasicNoResultIv.visibility = View.GONE
//        binding.historyBasicNoResultTv.visibility = View.GONE
        val result = response.list
        if(searchText!=null) {
            recyclerViewAdapter.cleardlItems()
            recyclerViewAdapter.notifyDataSetChanged()
        }
        recyclerViewAdapter.setdlItems(result)
        recyclerViewAdapter.notifyItemRangeInserted((requiredPageNum-1)*20,(requiredPageNum-1)*20+pageInfo.dataNum_currentPage!!)
        if (pageInfo.hasNext!!){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onHistoryDiaryFailure(code: Int, message: String, recyclerViewAdapter: HistoryBasicRecyclerViewAdapter) {
//        binding.historyBasicLoadingPb.visibility = View.GONE
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
        disposable?.dispose()
    }
}

