package com.likefirst.btos.ui.history

import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.data.entities.HistoryDetail
import com.likefirst.btos.databinding.ActivityHistoryDetailBinding
import com.likefirst.btos.ui.BaseActivity

class HistoryDetailActivity : BaseActivity<ActivityHistoryDetailBinding>(
    ActivityHistoryDetailBinding::inflate) {

    val items = List(100, { i -> HistoryDetail(i, "부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자. " +
            "부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.", "2021.12.12", "처음이") })

    override fun initAfterBinding() {

        binding.historyDetailToolbar.apply {
            historyDetailBackIv.setOnClickListener { finish() }
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
}