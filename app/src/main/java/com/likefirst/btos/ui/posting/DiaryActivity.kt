package com.likefirst.btos.ui.posting

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.entities.PostDiaryRequest
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.view.PostDiaryView
import com.likefirst.btos.databinding.ActivityDiaryBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.saveLastPostingDate
import java.util.*
import kotlin.collections.ArrayList

class DiaryActivity() : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate), PostDiaryView {

    companion object{
        var emotionIdx = 0
        var doneLists = ArrayList<String>()
        var contents = ""
    }
    @SuppressLint("Recycle")
    override fun initAfterBinding() {
        //TODO: 프리미엄 회원 확인해서 뷰 다르게 보여주기

        // companion object 초기화
        emotionIdx = 0
        doneLists = arrayListOf()
        contents = ""

        // contents 초기화
        initContents()

        // 이모션 리사이클러뷰 생성
        initEmotionRv()

        //DoneList 리사이클러뷰 연결
        initDoneListRv()

        // 툴바 동작구현
        setToolbar()

        binding.diaryContentsEt.addTextChangedListener(object :  TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(null !=  binding.diaryContentsEt.layout && binding.diaryContentsEt.layout.lineCount > 100){
                    binding.diaryContentsEt.text.delete(binding.diaryContentsEt.selectionStart - 1, binding.diaryContentsEt.selectionStart)
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initContents(){
        binding.diaryDateTv.text = intent.getStringExtra("diaryDate")
    }

    fun setToolbar(){
        //툴바 버튼 동작구현
        binding.diaryToolbar.diaryBackIv.setOnClickListener {
            onBackPressed()
        }

        binding.diaryToolbar.diaryToggleIv.setOnClickListener {
            val isPublic = isPublic()
            diaryToggleSwitcher(isPublic)
        }

        binding.diaryToolbar.diaryCheckIv.setOnClickListener {
            contents = binding.diaryContentsEt.text.toString()
            diaryValidationCheck()
            if (diaryValidationCheck()){
                val diaryDate = binding.diaryDateTv.text.toString()
                val diaryRequest = PostDiaryRequest(getUserIdx(), emotionIdx, diaryDate, contents, isPublic(), doneLists)
                if(isPublic()){
                    val dialog = CustomDialogFragment()
                    val data = arrayOf("취소", "확인")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "일기를 공개로 작성할까요? 일기를 공개로 작성하면 랜덤한 사람에게 보내집니다. 보낸 일기는 오후 7시 전까지만 수정, 삭제할 수 있습니다.",
                        "btnData" to data
                    )
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {

                        }
                        override fun onButton2Clicked() {
                            val diaryService = DiaryService()
                            diaryService.setPostDiaryView(this@DiaryActivity)
                            diaryService.postDiary(diaryRequest)
                        }
                    })
                    dialog.show(this.supportFragmentManager, "PublicAlertDialog")
                } else {
                    val diaryService = DiaryService()
                    diaryService.setPostDiaryView(this@DiaryActivity)
                    diaryService.postDiary(diaryRequest)
                }
            }
        }
    }

    fun initDoneListRv(){
        val doneListAdapter = DiaryDoneListRVAdapter("diary")
        binding.diaryDoneListRv.apply{
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            itemAnimator = null
        }

        //doneList 엔터 입력 시 리사이클러뷰 갱신
        binding.diaryDoneListEt.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.diaryDoneListEt.setOnKeyListener { p0, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                if(doneListAdapter.doneLists.size >= 10){
                    binding.diaryDoneListEt.text.delete( binding.diaryDoneListEt.selectionStart - 1, binding.diaryDoneListEt.selectionStart)
                    showOneBtnDialog("오늘하루 정말 알차게 사셨군요!! 아쉽지만 오늘 한 일은 10개까지만 작성이 가능합니다. 내일 또 봐요!", "doneListFullAlert")
                } else {
                    if(binding.diaryDoneListEt.text.length < 100){
                        binding.diaryDoneListEt.text.delete( binding.diaryDoneListEt.selectionStart - 1, binding.diaryDoneListEt.selectionStart)
                    }
                    if(TextUtils.isEmpty(binding.diaryDoneListEt.text)){
                        showOneBtnDialog("오늘 한 일을 입력해 주세요!", "doneListNullAlert")
                    } else {
                        doneListAdapter.addDoneList(binding.diaryDoneListEt.text.toString())
                        binding.diaryDoneListEt.text = null
                        binding.diaryDoneListEt.setSelection(0)
                    }
                }
            }
            false
        }
    }

    fun showOneBtnDialog(message : String, tag : String){
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to  message,
            "btnData" to data
        )
        dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
            override fun onButton1Clicked() {

            }

            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.supportFragmentManager, tag)
    }

    fun initEmotionRv(){
        val emotionColorIds = ArrayList<Int>()
        val emotionGrayIds = ArrayList<Int>()
        val emotionNames = resources.getStringArray(com.likefirst.btos.R.array.emotionNames)
        for (num in 1..8){
            val emotionColorId = resources.getIdentifier("emotion$num", "drawable", this.packageName)
            emotionColorIds.add(emotionColorId)
            val emotionGrayId = resources.getIdentifier("emotion$num"+"_gray", "drawable", this.packageName)
            emotionGrayIds.add(emotionGrayId)
        }
        val emotionAdapter = DiaryEmotionRVAdapter(emotionColorIds, emotionGrayIds, emotionNames)
        val emotionDecoration = DiaryEmotionRVItemDecoration()
        emotionDecoration.setSize(this)
        binding.diaryEmotionsRv.apply {
            adapter = emotionAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setItemViewCacheSize(8)
            addItemDecoration(emotionDecoration)
        }
    }

    fun goToDiaryViewer(){
        val diaryDate = binding.diaryDateTv.text.toString()
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val intent = Intent(this, DiaryViewerActivity::class.java)
        intent.putExtra("diaryInfo", DiaryViewerInfo(userDB.getNickName()!!, emotionIdx, diaryDate, contents, isPublic(), doneLists))
        startActivity(intent)
    }

    fun diaryValidationCheck() : Boolean{
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        if (userDB.getUser().premium == "premium"){
            if (emotionIdx == 0) {
                showOneBtnDialog("감정이모티콘을 하나 선택해 주세요.", "No Emotion Check")
                return false
            }
        }
        if (contents == "") {
            showOneBtnDialog("일기를 한 글자라도 작성해 주세요!!", "No Contents Check")
            return false
        }
        return true
    }

    fun isPublic() : Boolean{
        return binding.diaryToolbar.diaryToggleTv.text == "공개"
    }

    fun diaryToggleSwitcher(isPublic : Boolean){
        return if (isPublic){
            binding.diaryToolbar.diaryToggleIv.setImageResource(R.drawable.ic_toggle_false)
            binding.diaryToolbar.diaryToggleTv.text = "비공개"
            binding.diaryToolbar.diaryToggleSelector.visibility = View.INVISIBLE
        } else {
            binding.diaryToolbar.diaryToggleIv.setImageResource(R.drawable.ic_toggle_true)
            binding.diaryToolbar.diaryToggleTv.text = "공개"
            binding.diaryToolbar.diaryToggleSelector.visibility = View.VISIBLE
        }
    }

    override fun onDiaryPostLoading() {
        //TODO: 로딩화면 처리

    }

    override fun onDiaryPostSuccess() {
        goToDiaryViewer()
        saveLastPostingDate(Date())
    }

    override fun onDiaryPostFailure(code: Int) {
        when (code){
            4000, 7012, 7013 -> {
                showOneBtnDialog("데이터베이스 연결에 실패하였습니다. 다시 시도해 주세요.", "onDiaryPostFailure Code:4000")
            }
            6000 ->{
                showOneBtnDialog("일기는 하루에 하나만 작성 가능합니다.", "onDiaryPostFailure Code:6000")
            }
            6001 -> {
                showOneBtnDialog("오늘 작성한 일기만 공개설정하여 타인에게 전송할 수 있습니다.", "onDiaryPostFailure Code:6001")
            }

        }
    }
}