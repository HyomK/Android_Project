package com.likefirst.btos.ui.posting

import android.annotation.SuppressLint
import android.content.res.Resources
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityDiaryBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment

class DiaryActivity : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate) {
    @SuppressLint("Recycle")
    override fun initAfterBinding() {

        // 이모션 리사이클러뷰 생성
        val emotionColorIds = ArrayList<Int>()
        val emotionGrayIds = ArrayList<Int>()
        val emotionNames = resources.getStringArray(R.array.emotionNames)
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

        val doneListAdapter = DiaryDoneListRVAdapter()
        binding.diaryDoneListRv.apply{
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        //doneList 엔터 입력 시 리사이클러뷰 갱신
        binding.diaryDoneListEt.setOnKeyListener { p0, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                binding.diaryDoneListEt.text.delete( binding.diaryDoneListEt.text.length - 1, binding.diaryDoneListEt.text.length)
                doneListAdapter.addDoneList(binding.diaryDoneListEt.text.toString())
                Log.d("string", binding.diaryDoneListEt.text.toString())
                binding.diaryDoneListEt.text = null
                binding.diaryDoneListEt.setSelection(0)
            }
            false
        }

        binding.diaryDoneListEt.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (null !=  binding.diaryDoneListEt.layout && binding.diaryDoneListEt.layout.lineCount > 2) {
                    binding.diaryDoneListEt.text.delete( binding.diaryDoneListEt.text.length - 1, binding.diaryDoneListEt.text.length)
                }
            }
        })

        //툴바 버튼 동작구현
        binding.diaryToolbar.diaryBackIv.setOnClickListener {
            onBackPressed()
        }

        binding.diaryToolbar.diaryToggleIv.setOnClickListener {
            val isPublic = isPublic()
            diaryToggleSwitcher(isPublic)
        }

        binding.diaryToolbar.diaryCheckIv.setOnClickListener {
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

                    }
                })
                dialog.show(this.supportFragmentManager, "CustomDialog")
            } else {
                startActivityWithClear(DiaryViewerActivity::class.java)
            }
        }
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
}