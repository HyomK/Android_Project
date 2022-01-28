package com.likefirst.btos.ui.history

import android.util.Log
import android.view.View
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.ui.BaseFragment

class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    override fun initAfterBinding() {

        binding.historyRadiobuttonFirst.isChecked = true

        binding.historyToolbar.historyBackIv.setOnClickListener {

            if(binding.historyToolbar.historySearchEt.visibility == View.GONE){

                // 발신인 상세화면에서 뒤로가기를 누른 경우 (HistoryFragment로 돌아와야 한다)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,SenderFragment(), "historysender")
                    .commit()
                binding.historyToolbar.historyBackIv.visibility = View.GONE
                binding.historyToolbar.historySearchIv.visibility = View.VISIBLE
            }
            else{

                // 검색 화면에서 뒤로가기를 누른 경우 (Edittext와 뒤로가기 버튼이 없어지며 HistoryFragment로 돌아온다.)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,SenderFragment(), "historysender")
                    .commit()
                binding.historyToolbar.historyBackIv.visibility = View.GONE
                binding.historyToolbar.historySearchEt.visibility = View.GONE
            }
        }

        binding.historyToolbar.historySearchIv.setOnClickListener {
            // 검색 버튼을 누른 경우
            binding.historyToolbar.historySearchEt.visibility = View.VISIBLE
            binding.historyToolbar.historyBackIv.visibility = View.VISIBLE
        }

        binding.historyRadiogroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.history_radiobutton_first->{
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.history_fragment,SenderFragment(), "historysender")
                        .commit()
                }
                R.id.history_radiobutton_second-> {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.history_fragment, DiaryFragment(), "historydiary")
                        .commit()
                }
                R.id.history_radiobutton_third-> {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.history_fragment, MailFragment(), "historymail")
                        .commit()
                }
            }
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if(isHidden && isAdded){
            Log.d("listFragment", requireActivity().supportFragmentManager.fragments.toString())
            val fragments = arrayOf("historydiary","historymail","senderdetail","history")
            fragments.forEach { fragment ->
                requireActivity().supportFragmentManager.commit{
                    requireActivity().supportFragmentManager
                        .findFragmentByTag(fragment)?.let { remove(it) }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        binding.historyToolbar.historyBackIv.visibility = View.GONE
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.history_fragment,SenderFragment(), "historysender")
            .commit()
    }

}
