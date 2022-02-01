package com.likefirst.btos.ui.posting

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.utils.toArrayList


import com.likefirst.btos.data.entities.DiaryInfo
import com.likefirst.btos.data.remote.response.Diary
import com.likefirst.btos.databinding.ActivityDiaryViewerBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity

class DiaryViewerActivity: BaseActivity<ActivityDiaryViewerBinding>(ActivityDiaryViewerBinding::inflate) {
    override fun initAfterBinding() {

        binding.diaryViewerToolbar.toolbarBackIc.setOnClickListener {
            goToHome()
        }
        initView()
        initToolbar()
    }

    fun initView(){
        val intentDataset = intent.getParcelableExtra<DiaryInfo>("diaryInfo")!!
        val emotionNames = resources.getStringArray(R.array.emotionNames)
        val emotionIdx = intentDataset.emotionIdx
        if(emotionIdx != null){
            val emotionImgRes = resources.getIdentifier("emotion"+(emotionIdx+1).toString(), "drawable", this.packageName)
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
    }

    fun initToolbar(){
        binding.diaryViewerMoreIv.setOnClickListener {
            //TODO: 공개/비공개 전환
            //  삭제 기능 구현
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
        goToHome()
        super.onBackPressed()
    }

    fun goToHome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}