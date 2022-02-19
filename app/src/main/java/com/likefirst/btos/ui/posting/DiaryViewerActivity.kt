package com.likefirst.btos.ui.posting

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.databinding.ActivityDiaryViewerBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity


class DiaryViewerActivity: BaseActivity<ActivityDiaryViewerBinding>(ActivityDiaryViewerBinding::inflate) {
    override fun initAfterBinding() {

//        binding.diaryViewerToolbar.diaryViewerBackIv.setOnClickListener {
//            onBackPressed()
//        }
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
//        binding.diaryViewerToolbar.diaryViewerMoreIv.setOnClickListener {
//            //TODO: 공개/비공개 전환
//            //  삭제 기능 구현
//       }

//        binding.diaryViewerToolbar.diaryViewerEditIv.setOnClickListener{
//            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
//            val intentViewer = Intent(this, DiaryActivity::class.java)
//            intentViewer.putExtra("diaryInfo", intentDataset)
//            intentViewer.putExtra("editingMode", true)
//            intentViewer.putExtra("selectedPosition", intent.getIntExtra("selectedPosition", -1))
//            intentViewer.putExtra("diaryIdx", intent.getIntExtra("diaryIdx", 0))
//            startActivity(intentViewer)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.likefirst.btos.R.menu.diary_viewer_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            com.likefirst.btos.R.id.diary_viewer_option_delete -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setDoneListRv(doneLists : ArrayList<String>){

        val doneListAdapter = DiaryViewerDoneListRVAdapter(doneLists)

        binding.diaryViewerDoneListRv.apply {
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    override fun onBackPressed() {
        val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
        val mIntent = Intent(this, MainActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mIntent.putExtra("diaryInfo", intentDataset)
        mIntent.putExtra("isDiaryUpdated", intent.getBooleanExtra("isUpdated", false))
        mIntent.putExtra("position", intent.getIntExtra("selectedPosition", -1))
        startActivity(mIntent)

        super.onBackPressed()
    }
}