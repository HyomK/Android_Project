package com.likefirst.btos.ui.posting

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityDiaryBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity

class DiaryActivity : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate) {
    override fun initAfterBinding() {

        val doneListAdapter = DiaryDoneListRVAdapter()
        binding.diaryDoneListRv.apply{
            adapter = doneListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        //doneList 엔터 입력 시 리사이클러뷰 갱신
        binding.diaryDoneListEt.setOnKeyListener { p0, keyCode, p2 ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    doneListAdapter.addDoneList(binding.diaryDoneListEt.text.toString())
                    binding.diaryDoneListEt.text = null
                }
            }
            false
        }

        binding.diaryDoneListEt.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.diaryDoneListEt.lineCount >= 3){
                    binding.diaryDoneListEt.setText(p0)
                    binding.diaryDoneListEt.setSelection(1)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        //툴바 버튼 동작구현
        binding.diaryToolbar.diaryBackIv.setOnClickListener {
            onBackPressed()
        }

        binding.diaryToolbar.diaryToggleIv.setOnClickListener {
            var isPublic = false
            if(binding.diaryToolbar.diaryToggleTv.text == "공개"){
                isPublic = true
            }
            diaryToggleSwitcher(isPublic)
        }

        binding.diaryToolbar.diaryCheckIv.setOnClickListener {
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
        }
    }

    fun diaryToggleSwitcher(isPublic : Boolean) : Boolean{
        return if (isPublic){
            binding.diaryToolbar.diaryToggleIv.setImageResource(R.drawable.ic_toggle_false)
            binding.diaryToolbar.diaryToggleTv.text = "비공개"
            binding.diaryToolbar.diaryToggleSelector.visibility = View.INVISIBLE
            false
        } else {
            binding.diaryToolbar.diaryToggleIv.setImageResource(R.drawable.ic_toggle_true)
            binding.diaryToolbar.diaryToggleTv.text = "공개"
            binding.diaryToolbar.diaryToggleSelector.visibility = View.VISIBLE
            true
        }
    }
}