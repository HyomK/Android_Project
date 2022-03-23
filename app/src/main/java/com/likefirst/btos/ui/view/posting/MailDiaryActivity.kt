package com.likefirst.btos.ui.view.posting

import android.content.Intent
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.databinding.ActivityDiaryViewerBinding
import com.likefirst.btos.ui.BaseActivity

class MailDiaryActivity : BaseActivity<ActivityDiaryViewerBinding>(ActivityDiaryViewerBinding::inflate){

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


    fun setFont(fontIdx : Int){
        val fontList = resources.getStringArray(R.array.fontEng)
        val font = resources.getIdentifier(fontList[fontIdx], "font", this.packageName)
        binding.diaryViewerNameTv.typeface = ResourcesCompat.getFont(this,font)
        binding.diaryViewerEmotionTv.typeface = ResourcesCompat.getFont(this,font)
        binding.diaryViewerDateTv.typeface = ResourcesCompat.getFont(this,font)
        binding.diaryViewerContentsTv.typeface = ResourcesCompat.getFont(this,font)
    }

    fun initView(){
        val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
        val emotionNames = resources.getStringArray(com.likefirst.btos.R.array.emotionNames)
        val emotionIdx = intentDataset.emotionIdx
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val emotionImgRes = resources.getIdentifier("emotion"+(emotionIdx).toString(), "drawable", this.packageName)
        binding.diaryViewerEmotionIv.apply {
            visibility = View.VISIBLE
            setImageResource(emotionImgRes)
        }
        binding.diaryViewerEmotionTv.apply {
            visibility = View.VISIBLE
            binding.diaryViewerEmotionTv.text = emotionNames[emotionIdx]
        }
        setDoneListRv(intentDataset.doneLists)
        binding.diaryViewerContentsTv.text = intentDataset.contents
        binding.diaryViewerDateTv.text = intentDataset.diaryDate
        binding.diaryViewerNameTv.text = intentDataset.userName
        setFont(userDB.getFontIdx()!!)
    }

    fun initToolbar(){
        setSupportActionBar(findViewById(com.likefirst.btos.R.id.diary_viewer_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 타이틀 이름 안보이게
        binding.diaryViewerToolbar.diaryViewerEditIv.visibility=View.GONE
        binding.diaryViewerToolbar.diaryViewerMoreIv.visibility=View.GONE
    }

    fun setDoneListRv(doneLists : ArrayList<String>){
        val userDB = UserDatabase.getInstance(this)!!.userDao()
        val doneListAdapter = DiaryViewerDoneListRVAdapter(doneLists, this, userDB.getFontIdx()!!)

        binding.diaryViewerDoneListRv.apply {
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

}
