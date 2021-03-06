package com.likefirst.btos.ui.view.posting

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.entities.PostDiaryRequest
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.posting.response.PostDiaryResponse
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.view.PostDiaryView
import com.likefirst.btos.data.remote.posting.view.UpdateDiaryView
import com.likefirst.btos.data.remote.viewer.response.UpdateDiaryRequest
import com.likefirst.btos.databinding.ActivityDiaryBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.view.main.CustomDialogFragment
import com.likefirst.btos.ui.view.splash.LoginActivity
import com.likefirst.btos.utils.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class DiaryActivity() : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate), PostDiaryView, UpdateDiaryView {

    companion object{
        var emotionIdx = -1  // 이모션 선택할 때마다 리사이클러뷰 어댑터에서 자동으로 설정해줌
        var doneLists = ArrayList<String>()     // 입력할 때마다 리사이클러뷰 어댑터에서 자동으로 설정해줌
        var contents = ""   // 입력할 때마다 edittextlistener달아서 자동으로 설정해줌
    }
    @SuppressLint("Recycle")
    override fun initAfterBinding() {

        // companion object 초기화
        emotionIdx = -1
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

    fun setFont(fontIdx : Int){
        val fontList = resources.getStringArray(R.array.fontEng)
        val font = resources.getIdentifier(fontList[fontIdx], "font", this.packageName)
        binding.diaryContentsEt.typeface = ResourcesCompat.getFont(this,font)
        binding.diaryDoneListEt.typeface = ResourcesCompat.getFont(this,font)
        binding.diaryDateTv.typeface = ResourcesCompat.getFont(this,font)
    }

    fun initContents(){
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        setFont(userDB.getFontIdx()!!)

        // 일기 수정모드일 때 contents set
        if(intent.getBooleanExtra("editingMode", false) &&
            intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){

            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")
            binding.diaryContentsEt.setText(intentDataset!!.contents)
            binding.diaryDateTv.text = intentDataset.diaryDate
        } else {
            // 일기 수정모드가 아닐 때 contents 초기화
            binding.diaryDateTv.text = intent.getStringExtra("diaryDate")
        }

    }

    fun setToolbar(){
        //툴바 버튼 동작구현
        binding.diaryToolbar.diaryBackIv.setOnClickListener {
            onBackPressed()
        }

        // 과거일기의 경우 공개비공개 설정 버튼 안보이게
        val millisNow = System.currentTimeMillis()
        val millisDiary = stringToDate(binding.diaryDateTv.text.toString()).time
        if((millisNow-millisDiary) >= (1000*60*60*43)){
            binding.diaryToolbar.diaryToggleSelector.visibility = View.GONE
            binding.diaryToolbar.diaryToggleTv.visibility = View.GONE
            binding.diaryToolbar.diaryToggleIv.visibility = View.GONE
            diaryToggleSwitcher(true)
        } else {
            // 일기 수정모드일 때 토글버튼 set
            if(intent.getBooleanExtra("editingMode", false) &&
                intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){
                val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")
                diaryToggleSwitcher(!intentDataset!!.isPublic)
            }
            binding.diaryToolbar.diaryToggleIv.setOnClickListener {
                val isPublic = isPublic()
                diaryToggleSwitcher(isPublic)
            }
        }

        binding.diaryToolbar.diaryCheckIv.setOnClickListener {
            contents = binding.diaryContentsEt.text.toString()
            diaryValidationCheck()
            if (diaryValidationCheck()){
                val diaryDate = binding.diaryDateTv.text.toString()
                if(isPublic()){
                    val dialog = CustomDialogFragment()
                    val data = arrayOf("취소", "확인")
                    dialog.arguments= bundleOf(
                        "bodyContext" to "일기를 공개로 작성할까요? 일기를 공개로 작성하면 랜덤한 사람에게 보내집니다. 공개작성된 일기는 내일 오후 7시 전까지만 비공개로 전환 할 수 있습니다.",
                        "btnData" to data
                    )
                    dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                        override fun onButton1Clicked() {

                        }
                        override fun onButton2Clicked() {
                            if(intent.getBooleanExtra("editingMode", false)
                                && intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){
                                updateDiary()
                            } else {
                                postDiary()
                            }
                        }
                    })
                    dialog.show(this.supportFragmentManager, "PublicAlertDialog")
                } else {
                    if(intent.getBooleanExtra("editingMode", false)
                        && intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){
                        updateDiary()
                        } else {
                        postDiary()
                    }
                }
            }
        }
    }

    fun initDoneListRv(){
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val doneListAdapter = DiaryDoneListRVAdapter("diary", this, userDB.getFontIdx()!!)
        binding.diaryDoneListRv.apply{
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            itemAnimator = null
        }
        doneListAdapter.setOnDoneListEnter(object : DiaryDoneListRVAdapter.ItemClickListener{
            override fun onDoneListEnter(view : View) {
                hideKeyboard(view)
            }

        })

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

        // 일기 수정모드일 때 doneList set
        if(intent.getBooleanExtra("editingMode", false) &&
            intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){
            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")
            for (i in 0 until intentDataset!!.doneLists.size){
                doneListAdapter.addDoneList(intentDataset.doneLists[i])
            }
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
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val emotionColorIds = ArrayList<Int>()
        val emotionGrayIds = ArrayList<Int>()
        val emotionNames = resources.getStringArray(com.likefirst.btos.R.array.emotionNames)
        for (num in 0..7){
            val emotionColorId = resources.getIdentifier("emotion$num", "drawable", this.packageName)
            emotionColorIds.add(emotionColorId)
            val emotionGrayId = resources.getIdentifier("emotion$num"+"_gray", "drawable", this.packageName)
            emotionGrayIds.add(emotionGrayId)
        }
        var emotionAdapter = DiaryEmotionRVAdapter(emotionColorIds, emotionGrayIds, emotionNames, null, this, userDB.getFontIdx()!!)
        // 수정모드일 때 emotion리사이클러뷰 하나 선택되어있는 상태의 어댑터로 변경
        if(intent.getBooleanExtra("editingMode", false) &&
            intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null){
            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")
            emotionAdapter = DiaryEmotionRVAdapter(emotionColorIds, emotionGrayIds, emotionNames, intentDataset!!.emotionIdx, this, userDB.getFontIdx()!!)
            emotionIdx = intentDataset.emotionIdx
        }
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

    fun postDiary(){
        val diaryDate = binding.diaryDateTv.text.toString()
        val diaryRequest = PostDiaryRequest(getUserIdx(), emotionIdx, diaryDate, contents, isPublic(), doneLists)
        val diaryService = DiaryService()
        diaryService.setPostDiaryView(this@DiaryActivity)
        diaryService.postDiary(diaryRequest)
    }

    fun updateDiary(){
        val diaryDate = binding.diaryDateTv.text.toString()
        val diaryIdx = intent.getIntExtra("diaryIdx", 0)
        Log.d("donelist", doneLists.toString())
        var isPublic = 0
        if(isPublic()){
            isPublic = 1
        }
        val diaryService = DiaryService()
        diaryService.setUpdateDiaryView(this)
        diaryService.updateDiary(UpdateDiaryRequest(diaryIdx, getUserIdx(), emotionIdx, diaryDate, contents, isPublic, doneLists))
    }

    fun goToDiaryViewer(newDiaryIdx : Int){
        val selectedPosition = intent.getIntExtra("selectedPosition", -1)
        val diaryDate = binding.diaryDateTv.text.toString()
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val mIntent = Intent(this, DiaryViewerActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mIntent.putExtra("selectedPosition", selectedPosition)
        mIntent.putExtra("diaryIdx", intent.getIntExtra("diaryIdx", newDiaryIdx))
        mIntent.putExtra("diaryInfo", DiaryViewerInfo(userDB.getNickName()!!, emotionIdx, diaryDate, contents, isPublic(), doneLists))
        startActivity(mIntent)
    }

    fun diaryValidationCheck() : Boolean{
        if (emotionIdx == -1) {
            showOneBtnDialog("감정이모티콘을 하나 선택해 주세요.", "No Emotion Check")
            return false
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

    override fun onBackPressed() {
        val dialog = CustomDialogFragment()
        val data = arrayOf("취소", "확인")
        dialog.arguments= bundleOf(
            "bodyContext" to  "일기 작성을 취소할까요?",
            "btnData" to data
        )
        dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
            override fun onButton1Clicked() {

            }

            override fun onButton2Clicked() {
                finish()
            }
        })
        dialog.show(this.supportFragmentManager, "Cancel Diary Writing dialog")
    }

    override fun onDiaryPostLoading() {
        binding.diaryLoadingView.apply{
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    override fun onDiaryPostSuccess(result : PostDiaryResponse) {
        binding.diaryLoadingView.visibility = View.GONE
        goToDiaryViewer(result.diaryIdx)
        DiaryViewerActivity.diaryStateFlag = DiaryViewerActivity.CREATE
        if (dateToString(Date()) == binding.diaryDateTv.text.toString()){
            saveLastPostingDate(Date())
        }
    }

    override fun onDiaryPostFailure(code: Int) {
        binding.diaryLoadingView.visibility = View.GONE
        when (code){
            4000, 7012, 7013 -> {
                showOneBtnDialog("데이터베이스 연결에 실패하였습니다. 다시 시도해 주세요.", "onDiaryPostFailure Code:4000")
            }
            6000 ->{
                val dialog = CustomDialogFragment()
                val data = arrayOf("확인")
                dialog.arguments= bundleOf(
                    "bodyContext" to  "유효하지 않은 회원정보입니다. 다시 로그인 해주세요",
                    "btnData" to data
                )
                dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
                    override fun onButton1Clicked() {
                        val gso = getGSO()
                        val googleSignInClient = GoogleSignIn.getClient(this@DiaryActivity, gso)
                        googleSignInClient.signOut()
                        removeJwt()
                        val intent = Intent(this@DiaryActivity, LoginActivity::class.java)
                        startActivity(intent)
                        exitProcess(0)
                    }

                    override fun onButton2Clicked() {

                    }
                })
                dialog.show(this.supportFragmentManager, "onDiaryPostFailure Code:6000")
            }
            6009 -> {
                showOneBtnDialog("해당 날짜에 이미 일기를 작성하셨습니다.", "onDiaryPostFailure Code:6003")
            }
            6010 -> {
                showOneBtnDialog("오늘 작성한 일기만 공개설정하여 타인에게 전송할 수 있습니다.", "onDiaryPostFailure Code:6001")
            }
        }
    }

    override fun onArchiveUpdateLoading() {
        binding.diaryLoadingView.apply{
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    override fun onArchiveUpdateSuccess() {
        binding.diaryLoadingView.visibility = View.GONE
        goToDiaryViewer(0)
        DiaryViewerActivity.diaryStateFlag = DiaryViewerActivity.UPDATE
    }

    override fun onArchiveUpdateFailure(code: Int) {
        binding.diaryLoadingView.visibility = View.GONE
        when (code){
            4000 -> {
                showOneBtnDialog("데이터베이스 연결에 실패하였습니다. 다시 시도해 주세요.", "onDiaryPostFailure Code:4000")
            }
            6000 -> {
                val dialog = CustomDialogFragment()
                val data = arrayOf("확인")
                dialog.arguments= bundleOf(
                    "bodyContext" to  "유효하지 않은 회원정보입니다. 다시 로그인 해주세요",
                    "btnData" to data
                )
                dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
                    override fun onButton1Clicked() {
                        val gso = getGSO()
                        val googleSignInClient = GoogleSignIn.getClient(this@DiaryActivity, gso)
                        googleSignInClient.signOut()
                        removeJwt()
                        val intent = Intent(this@DiaryActivity, LoginActivity::class.java)
                        startActivity(intent)
                        exitProcess(0)
                    }

                    override fun onButton2Clicked() {

                    }
                })
                dialog.show(this.supportFragmentManager, "onDiaryUpdateFailure Code:6000")
            }
            6002 -> showOneBtnDialog("존재하지 않는 일기 입니다. 개발자에게 문의해 주세요", "onDiaryPostFailure Code:6002")
            6003 -> showOneBtnDialog("해당 일기에 접근 권한이 없습니다. 개발자에게 문의해 주세요", "onDiaryPostFailure Code:6003")
            6009 -> showOneBtnDialog("일기는 하루에 하나만 작성 가능합니다.", "onDiaryPostFailure Code:6009")
            6010 -> showOneBtnDialog("당일에 작성한 일기만 공개설정이 가능합니다.", "onDiaryPostFailure Code:6010")
            6012 -> showOneBtnDialog("일기 수정에 실패하였습니다.", "onDiaryPostFailure Code:6012")
            6013 -> showOneBtnDialog("doneList 수정에 실패하였습니다.", "onDiaryPostFailure Code:6013")
        }
    }
}