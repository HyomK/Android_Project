package com.likefirst.btos.ui.history

import android.view.View
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.ui.BaseFragment

class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    override fun initAfterBinding() {

        binding.historyToolbar.historySearchIv.setOnClickListener {
            // 검색 버튼을 누른 경우
            binding.historyToolbar.historySearchEt.visibility = View.VISIBLE
            binding.historyToolbar.historyBackIv.visibility = View.VISIBLE
        }
        binding.historyToolbar.historyBackIv.setOnClickListener {
            binding.historyToolbar.historySearchEt.visibility = View.GONE
            binding.historyToolbar.historyBackIv.visibility = View.GONE
        }

        binding.historyRadiogroup.setOnCheckedChangeListener { radioGroup, i ->
            radioButton(i)
        }
    }

    fun radioButton(id : Int){
        when (id) {
            R.id.history_radiobutton_first->{
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,HistoryBasicFragment("sender"), "historysender")
                    .commit()
            }
            R.id.history_radiobutton_second-> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment, HistoryBasicFragment("diary"), "historydiary")
                    .commit()
            }
            R.id.history_radiobutton_third-> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment, HistoryBasicFragment("letter"), "historymail")
                    .commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        radioButton(binding.historyRadiogroup.checkedRadioButtonId)
    }

}
