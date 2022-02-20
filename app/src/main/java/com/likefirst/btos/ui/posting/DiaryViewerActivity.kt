package com.likefirst.btos.ui.posting

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.view.DeleteDiaryView
import com.likefirst.btos.databinding.ActivityDiaryViewerBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.splash.LoginActivity
import com.likefirst.btos.utils.getGSO
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.removeJwt
import kotlin.system.exitProcess


class DiaryViewerActivity: BaseActivity<ActivityDiaryViewerBinding>(ActivityDiaryViewerBinding::inflate) , DeleteDiaryView {

    companion object{
        const val UPDATE = 0
        const val DELETE = 1
        const val CREATE = 2
        const val READ = 3
        var diaryStateFlag = READ
    }

    override fun initAfterBinding() {

        binding.diaryViewerToolbar.diaryViewerBackIv.setOnClickListener {
            onBackPressed()
        }
        initView()
        initToolbar()
    }

//    fun resumeView(emotionIdx : Int, intentDataset : DiaryViewerInfo){
//        val emotionNames = resources.getStringArray(R.array.emotionNames)
//        if(emotionIdx != 0){
//            val emotionImgRes = resources.getIdentifier("emotion"+(emotionIdx).toString(), "drawable", this.packageName)
//            binding.diaryViewerEmotionIv.apply {
//                visibility = View.VISIBLE
//                setImageResource(emotionImgRes)
//            }
//            binding.diaryViewerEmotionTv.apply {
//                visibility = View.VISIBLE
//                binding.diaryViewerEmotionTv.text = emotionNames[emotionIdx]
//            }
//        }
//        setDoneListRv(intentDataset.doneLists)
//        binding.diaryViewerContentsTv.text = intentDataset.contents
//        binding.diaryViewerDateTv.text = intentDataset.diaryDate
//        binding.diaryViewerNameTv.text = intentDataset.userName
//    }

    fun initView(){
        val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
        val emotionNames = resources.getStringArray(com.likefirst.btos.R.array.emotionNames)
        val emotionIdx = intentDataset.emotionIdx
        if(emotionIdx != 0){
            val emotionImgRes = resources.getIdentifier("emotion"+(emotionIdx).toString(), "drawable", this.packageName)
            binding.diaryViewerEmotionIv.apply {
                visibility = View.VISIBLE
                setImageResource(emotionImgRes)
            }
            binding.diaryViewerEmotionTv.apply {
                visibility = View.VISIBLE
                binding.diaryViewerEmotionTv.text = emotionNames[emotionIdx]
            }
        }
        setDoneListRv(intentDataset.doneLists)
        binding.diaryViewerContentsTv.text = intentDataset.contents
        binding.diaryViewerDateTv.text = intentDataset.diaryDate
        binding.diaryViewerNameTv.text = intentDataset.userName
    }

    fun initToolbar(){
        setSupportActionBar(findViewById(com.likefirst.btos.R.id.diary_viewer_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 타이틀 이름 안보이게
        binding.diaryViewerToolbar.diaryViewerMoreIv.setOnClickListener { it ->
            //TODO: 공개/비공개 전환
            val popupMenu = PopupMenu(this, it)
            menuInflater.inflate(R.menu.diary_viewer_option_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.diary_viewer_option_delete -> {
                        val diaryService = DiaryService()
                        diaryService.setDeleteDiaryView(this@DiaryViewerActivity)
                        diaryService.deleteDiary(intent.getIntExtra("diaryIdx", 0), getUserIdx())
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
       }

        binding.diaryViewerToolbar.diaryViewerEditIv.setOnClickListener{
            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
            val intentViewer = Intent(this, DiaryActivity::class.java)
            intentViewer.putExtra("diaryInfo", intentDataset)
            intentViewer.putExtra("editingMode", true)
            intentViewer.putExtra("selectedPosition", intent.getIntExtra("selectedPosition", -1))
            intentViewer.putExtra("diaryIdx", intent.getIntExtra("diaryIdx", 0))
            startActivity(intentViewer)
        }
    }

    fun setDoneListRv(doneLists : ArrayList<String>){

        val doneListAdapter = DiaryViewerDoneListRVAdapter(doneLists)

        binding.diaryViewerDoneListRv.apply {
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
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
                val googleSignInClient = GoogleSignIn.getClient(this@DiaryViewerActivity, gso)
                googleSignInClient.signOut()
                removeJwt()
                val intent = Intent(this@DiaryViewerActivity, LoginActivity::class.java)
                startActivity(intent)
                exitProcess(0)
            }

            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.supportFragmentManager, tag)
    }

    override fun onBackPressed() {
        val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
        val mIntent = Intent(this, MainActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mIntent.putExtra("diaryInfo", intentDataset)
        mIntent.putExtra("diaryStateFlag", diaryStateFlag)
        mIntent.putExtra("position", intent.getIntExtra("selectedPosition", -1))
        startActivity(mIntent)

        super.onBackPressed()
    }

    override fun onDiaryDeleteLoading() {
        binding.diaryViewerLoadingView.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    override fun onDiaryDeleteSuccess() {
        binding.diaryViewerLoadingView.visibility = View.GONE
        diaryStateFlag = DELETE
        val mIntent = Intent(this, MainActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mIntent.putExtra("diaryStateFlag", diaryStateFlag)
        startActivity(mIntent)
        finish()
        Snackbar.make(binding.diaryViewerLayout, "데이터베이스 연결에 ㅁㄴㅇㄻㅇ럼대롬ㅈ;댜곤얼ㄴ얼ㄴ어ㅏ론아럼뇌얼뫼어로민어로매젿고민얼풐,ㄴ어ㅗ밎ㄷ괸웈ㅌ츠,풐너오베ㅑㅈㄷ고얼밎ㄷ렁.", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDiaryDeleteFailure(code: Int) {
        binding.diaryViewerLoadingView.visibility = View.GONE
        when (code) {
            4000 -> Snackbar.make(binding.diaryViewerLayout, "데이터베이스 연결에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            6000 -> showLogoutDialog("유효하지 않은 회원정보입니다. 다시 로그인 해주세요", "onDiaryPostFailure Code:6000")
            6002 -> Snackbar.make(binding.diaryViewerLayout, "존재하지 않는 일기입니다.", Snackbar.LENGTH_SHORT).show()
            6003 -> showLogoutDialog("해당일기에 접근권한이 없습니다. 다시 로그인 해주세요. (계속해서 같은 문제가 발생한다면 개발자에게 문의해주세요.)", "onDiaryPostFailure Code:6000")
            6014, 6015 -> Snackbar.make(binding.diaryViewerLayout, "일기 삭제에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }
}
