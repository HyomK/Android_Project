package com.likefirst.btos.ui.posting

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.databinding.ActivityDiaryViewerBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity

class DiaryViewerActivity: BaseActivity<ActivityDiaryViewerBinding>(ActivityDiaryViewerBinding::inflate) {
    override fun initAfterBinding() {

        binding.diaryViewerToolbar.diaryViewerBackIv.setOnClickListener {
            onBackPressed()
        }
        initView()
        initToolbar()
    }

    fun initView(){
        val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
        val emotionNames = resources.getStringArray(R.array.emotionNames)
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
        binding.diaryViewerToolbar.diaryViewerMoreIv.setOnClickListener {
            //TODO: 공개/비공개 전환
            //  삭제 기능 구현
       }

        binding.diaryViewerToolbar.diaryViewerEditIv.setOnClickListener{
            val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
            val intent = Intent(this, DiaryActivity::class.java)
            intent.putExtra("diaryInfo", intentDataset)
            intent.putExtra("editingMode", true)
            startActivity(intent)
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

    override fun onBackPressed() {
        super.onBackPressed()
    }
}