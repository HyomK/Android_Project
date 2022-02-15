package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.viewer.response.ArchiveDiaryResult
import com.likefirst.btos.data.remote.viewer.response.ArchiveListPageInfo
import com.likefirst.btos.data.remote.viewer.response.ArchiveListResult
import com.likefirst.btos.data.remote.viewer.service.ArchiveDiaryService
import com.likefirst.btos.data.remote.viewer.service.ArchiveListService
import com.likefirst.btos.data.remote.viewer.view.ArchiveDiaryView
import com.likefirst.btos.data.remote.viewer.view.ArchiveListView
import com.likefirst.btos.databinding.FragmentArchiveListBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.splash.LoginActivity
import com.likefirst.btos.utils.getGSO
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.removeJwt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.system.exitProcess

class ArchiveListFragment : BaseFragment<FragmentArchiveListBinding>(FragmentArchiveListBinding::inflate),
    ArchiveDiaryView, ArchiveListView{

    lateinit var mAdapter: ArchiveListRVAdapter
    var selectedPosition = 0

    companion object{
        var requiredPageNum = 1
        var lastDate = ""
        var datePickerFlag = true
        var query = HashMap<String, String>()
    }

    override fun initAfterBinding() {
        //companion object 초기화
        requiredPageNum = 1
        lastDate = ""
        datePickerFlag = true
        query.clear()

        // 리사이클러뷰 상태유지
        mAdapter = ArchiveListRVAdapter(requireContext())
        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        initToolbar(mAdapter)
        initDatePicker(mAdapter)
        initDiaryList(mAdapter)
    }

    fun initDatePicker(mAdapter: ArchiveListRVAdapter){
        //datePicker 기능 설정
        binding.archiveListToolbar.archiveListPeriodIv.setOnClickListener {
            if(it.isSelected){
                query.remove("startDate")
                query.remove("endDate")
                reLoadDiaryList(mAdapter, query)
                it.isSelected = !it.isSelected
            } else {
                if(datePickerFlag){
                    val datePickerDialog = ArchiveListPeriodDialog.newInstance()
                    datePickerDialog.setDatePickerClickListener(object : ArchiveListPeriodDialog.DatePickerClickListener{
                        override fun onDatePicked(dateFrom: String, dateTo: String) {
                            query["startDate"] = dateFrom
                            query["endDate"] = dateTo
//                            binding.archiveListRv.clearOnScrollListeners()
//                            mAdapter.clearList()
                            reLoadDiaryList(mAdapter, query)
                            it.isSelected = !it.isSelected
                        }
                    })
                    datePickerDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
                    datePickerDialog.show(childFragmentManager, datePickerDialog.tag)
                    datePickerFlag = false
                }
            }
        }
    }

    fun reLoadDiaryList(mAdapter: ArchiveListRVAdapter, query: HashMap<String, String>){
        binding.archiveListRv.clearOnScrollListeners()
        binding.archiveListRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.archiveListRv.canScrollVertically(1) && !mAdapter.isDiaryEmpty()) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        binding.archiveListRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        loadDiaryList(requiredPageNum, mAdapter, query)
                    }
                }
            }
        })
        requiredPageNum = 1
        lastDate = ""
        mAdapter.clearList()
        Log.d("requiredPageNum", requiredPageNum.toString())
        loadDiaryList(requiredPageNum, mAdapter, query)
    }

    fun initDiaryList(mAdapter : ArchiveListRVAdapter){
        query.clear()
        val mDecoration = ArchiveListItemDecoration()
        mDecoration.setSize(requireContext())
        binding.archiveListRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.archiveListRv.canScrollVertically(1) && !mAdapter.isDiaryEmpty()) {
                    if(requiredPageNum == 0){
                        // 더이상 불러올 페이지가 없으면 스크롤 리스너 clear
                        binding.archiveListRv.clearOnScrollListeners()
                    } else {
                        // 다음 페이지 불러오기
                        loadDiaryList(requiredPageNum, mAdapter, query)
                    }
                }
            }
        })
        binding.archiveListRv.apply {
            adapter = mAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mAdapter.setDiarySelectedListener(object : ArchiveListRVAdapter.DiarySelectedListener{
                override fun onDiarySelect(diaryIdx : Int, position : Int) {
                    selectedPosition = position
                    val archiveDiaryService = ArchiveDiaryService()
                    archiveDiaryService.setArchiveCalendarView(this@ArchiveListFragment)
                    archiveDiaryService.getDiary(diaryIdx)
                }
            })
            if (itemDecorationCount == 0){
                addItemDecoration(mDecoration)
            }
        }
        loadDiaryList(requiredPageNum, mAdapter, query)
    }

    fun loadDiaryList(pageNum : Int, adapter : ArchiveListRVAdapter, query : HashMap<String,String>){
        // 검색조건에 따라 쿼리스트링 생성해서 전달
        val archiveListService = ArchiveListService()
        archiveListService.setArchiveListView(this)
        archiveListService.getList(getUserIdx(), pageNum, query, adapter)
    }

    fun initToolbar(mAdapter: ArchiveListRVAdapter){
        binding.archiveListToolbar.archiveListSearchIv.setOnClickListener {
            if (binding.archiveListToolbar.archiveListSearchEt.isVisible){
                query["search"] = binding.archiveListToolbar.archiveListSearchEt.text.toString()
                reLoadDiaryList(mAdapter, query)
            } else {
                binding.archiveListToolbar.archiveListSearchEt.apply {
                    visibility = View.VISIBLE
                    requestFocus()
                    val imm : InputMethodManager? = null
                }
                binding.archiveListToolbar.archiveListBackIv.visibility = View.VISIBLE
            }
        }
        binding.archiveListToolbar.archiveListBackIv.setOnClickListener {
            query.remove("search")
            reLoadDiaryList(mAdapter, query)
            binding.archiveListToolbar.archiveListSearchEt.visibility = View.GONE
            binding.archiveListToolbar.archiveListBackIv.visibility = View.GONE
        }
        binding.archiveListToolbar.archiveListSearchEt.setOnEditorActionListener { textView, actionId, keyEvent ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                query["search"] = textView.text.toString()
                reLoadDiaryList(mAdapter, query)
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }

    fun showLogoutDialog(message : String, tag : String){
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to  message,
            "btnData" to data
        )
        dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
            override fun onButton1Clicked() {
                val gso = getGSO()
                val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
                googleSignInClient.signOut()
                removeJwt()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                exitProcess(0)
            }

            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.parentFragmentManager, tag)
    }

//    private fun hideKeyboard() {
//        if (activity != null && requireActivity().currentFocus != null) {
//            // 프래그먼트기 때문에 requireActivity() 사용
//            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken,
//                InputMethodManager.HIDE_NOT_ALWAYS)
//        }
//    }

    override fun onArchiveListLoading() {
        when (requiredPageNum){
            1 -> {
                binding.archiveListNoSearchResultLayout.visibility = View.GONE
                binding.archiveListLoadingView.apply {
                    setAnimation("sprout_loading.json")
                    visibility = View.VISIBLE
                    playAnimation()
                }
            }
            else -> {
                binding.archiveListLoadingItem.apply {
                    setAnimation("sprout_loading.json")
                    visibility = View.VISIBLE
                    playAnimation()
                }
            }
        }

    }

    override fun onArchiveListSuccess(
        result: ArrayList<ArchiveListResult>,
        pageInfo: ArchiveListPageInfo,
        adapter: ArchiveListRVAdapter,
    ) {
        binding.archiveListNoSearchResultLayout.visibility = View.GONE
        binding.archiveListLoadingView.visibility = View.GONE
        binding.archiveListLoadingItem.visibility = View.GONE
        val startPosition = mAdapter.itemCount

        // result를 List RecyclerView에 뿌릴 diaryList로 변환
        val diaryList = arrayListOf<Any>()
        for (item in result){
            val topDate = item.month
            if(topDate != lastDate) {
                lastDate = topDate
                diaryList.add(item.month)
            }
            for (i in 0 until item.diaryList.size){
                diaryList.add(item.diaryList[i])
            }
        }
        adapter.apply{
            addDiaryList(diaryList)    //가공된 diaryList 어댑터에 추가
            notifyItemRangeInserted(startPosition, mAdapter.itemCount)
        }
        if (pageInfo.hasNext){
            requiredPageNum++
        } else {
            requiredPageNum = 0
        }
    }

    override fun onArchiveListFailure(code: Int) {
        binding.archiveListLoadingView.visibility = View.GONE
        when (code){
            4000 -> {
                Snackbar.make(requireView(), "데이터베이스 연결에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
            6000 -> {
                showLogoutDialog("유효하지 않은 회원입니다. 다시 로그인 해 주세요", "onDiaryGetFailure Code:6000")
            }
            6008 -> Snackbar.make(requireView(), "일기 복호화에 실패했습니다. 개발자에게 문의해 주세요.", Snackbar.LENGTH_SHORT).show()
            6016 -> Snackbar.make(requireView(), "페이지 번호는 1부터 시작해야 합니다.", Snackbar.LENGTH_SHORT).show()
            6017 -> Snackbar.make(requireView(), "잘못된 페이지 요청입니다. 개발자에게 문의해 주세요.", Snackbar.LENGTH_SHORT).show()
            6018 -> binding.archiveListNoSearchResultLayout.visibility = View.VISIBLE
        }
    }

    override fun onArchiveDiaryLoading() {
        binding.archiveListLoadingView.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    override fun onArchiveDiarySuccess(result: ArchiveDiaryResult) {
        binding.archiveListLoadingView.visibility = View.GONE
        var isPublic = false
        if (result.isPublic == 1){
            isPublic = true
        }
        val userDB = UserDatabase.getInstance(requireContext())!!.userDao()
        val intent = Intent(requireContext(), DiaryViewerActivity::class.java)
        intent.putExtra("diaryInfo", DiaryViewerInfo(userDB.getNickName()!!, result.emotionIdx, result.diaryDate, result.content, isPublic, result.doneList))
        intent.putExtra("diaryIdx", result.diaryIdx)
        intent.putExtra("selectedPosition", selectedPosition)
        startActivity(intent)
    }

    override fun onArchiveDiaryFailure(code: Int) {
        when (code){
            4000 -> Snackbar.make(requireView(), "데이터베이스 연결에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
            6002 -> Snackbar.make(requireView(), "존재하지 않는 일기입니다. 개발자에게 문의해 주세요.", Snackbar.LENGTH_SHORT).show()
            6008 -> Snackbar.make(requireView(), "일기 복호화에 실패했습니다. 개발자에게 문의해 주세요.", Snackbar.LENGTH_SHORT).show()
        }
    }
}