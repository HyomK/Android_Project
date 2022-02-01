package com.likefirst.btos.ui.history

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.data.entities.HistoryDetail
import com.likefirst.btos.databinding.ActivityHistoryDetailBinding
import com.likefirst.btos.ui.BaseActivity

class HistoryDetailActivity : BaseActivity<ActivityHistoryDetailBinding>(
    ActivityHistoryDetailBinding::inflate) {

    val items = List(20, { i -> HistoryDetail(i, "부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.\n"+
        "부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.", "2021.12.12", "처음이") })

    override fun initAfterBinding() {

        binding.historyDetailToolbar.apply {
            historyDetailBackIv.setOnClickListener {
                finish()
                val pos = intent.extras?.getString("backPos")
                val editor= getSharedPreferences("HistoryBackPos", MODE_PRIVATE).edit()
                editor.putString("backPos",pos)
                editor.commit()
            }
            historySendIv.setOnClickListener {

            }
            historyOptionIv.setOnClickListener {

            }
        }

        val recyclerViewAdapter = HistoryDetailRecyclerViewAdapter(this, items)
        binding.historyDetailRecyclerView.adapter = recyclerViewAdapter
        binding.historyDetailRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val pos = intent.extras?.getString("backPos")
        val editor= getSharedPreferences("HistoryBackPos", MODE_PRIVATE).edit()
        editor.putString("backPos",pos)
        editor.commit()
        Log.d("historyTagBack",pos.toString())

    }


}