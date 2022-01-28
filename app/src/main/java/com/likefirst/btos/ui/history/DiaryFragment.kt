package com.likefirst.btos.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.History
import com.likefirst.btos.databinding.FragmentHistoryDiaryBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class DiaryFragment: BaseFragment<FragmentHistoryDiaryBinding>(FragmentHistoryDiaryBinding::inflate) {

    val items = List(100, {i -> History(i,"2부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자." +
            "부족하면 부족한대로 채우고 충분하면 충분한대로 매력 발산하면서 멋지게 살자.","2021.12.12","처음이",1,3)  })

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        val recyclerViewAdapter = NoSenderRecyclerViewAdapter(context, items)
        binding.fragmentDiaryRv.adapter = recyclerViewAdapter
        binding.fragmentDiaryRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)

        recyclerViewAdapter.setMyItemClickListener(object : NoSenderRecyclerViewAdapter.MyItemClickListener{
            override fun MoveToDetail(historyIdx: Int) {
                mActivity.let {
                    val intent = Intent(context,HistoryDetailActivity::class.java)
                    val bundle = bundleOf("backPos" to "historydiary")
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        })
    }
}