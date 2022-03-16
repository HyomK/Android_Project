package com.likefirst.btos.presentation.View.history

import android.os.Bundle
import android.view.View
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.presentation.BaseFragment
import com.likefirst.btos.presentation.View.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    override fun initAfterBinding() {
        val mActivity = activity as MainActivity

        binding.historyToolbar.historySearchIv.setOnClickListener {
            // 검색 버튼을 누른 경우
            binding.historyToolbar.historySearchEt.visibility = View.VISIBLE
            binding.historyToolbar.historyBackIv.visibility = View.VISIBLE
            binding.historyToolbar.historySearchIv.isClickable = false
            if(binding.historyToolbar.historySearchEt.text.toString() != "") {
                binding.historyToolbar.historySearchEt.setText("")
            }
        }
        binding.historyToolbar.historyBackIv.setOnClickListener {
            checkET()
            checkView()
            mActivity.hideKeyboard(requireView())
        }
        binding.historyRadiogroup.setOnCheckedChangeListener { radioGroup, i ->
            checkView()
            mActivity.hideKeyboard(requireView())
            radioButton(i)
        }
    }

    fun radioButton(id : Int){
        when (id) {
            R.id.history_radiobutton_first->{
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,
                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","sender") } },
                        "historysender")
                    .commit()
            }
            R.id.history_radiobutton_second-> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,
                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","diary") } },
                        "historydiary")
                    .commit()
            }
            R.id.history_radiobutton_third-> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,
                        HistoryBasicFragment().apply { arguments = Bundle().apply { putString("filtering","letter") } },
                        "historymail")
                    .commit()
            }
        }
    }

    fun checkET(){
        if(binding.historyToolbar.historySearchEt.visibility == View.VISIBLE && binding.historyToolbar.historySearchEt.text.toString() != "") {
            binding.historyToolbar.historySearchEt.setText("")
        }
    }

    fun checkView(){
        binding.historyToolbar.historySearchEt.visibility = View.GONE
        binding.historyToolbar.historyBackIv.visibility = View.GONE
        binding.historyToolbar.historySearchIv.isClickable = true
    }

    override fun onStart() {
        super.onStart()
        radioButton(binding.historyRadiogroup.checkedRadioButtonId)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            checkView()
        }
    }
}
