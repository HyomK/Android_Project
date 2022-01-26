package com.likefirst.btos.ui.history

import android.view.View
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    override fun initAfterBinding() {

        binding.historyRadiobuttonFirst.isChecked = true

        val mActivity = activity as MainActivity
//        Log.e("FRAGMENT",mActivity.supportFragmentManager.findFragmentById(R.id.history_fragment).toString())
//        if(mActivity.supportFragmentManager.findFragmentById(R.id.history_fragment) == SenderDetailFragment()){
//            binding.historyToolbar.historyBackIv.visibility = View.VISIBLE
//        }

        binding.historyToolbar.historyBackIv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.history_fragment,SenderFragment(), "historysender")
                .commit()
            binding.historyToolbar.historyBackIv.visibility = View.GONE
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

    override fun onStart() {
        super.onStart()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.history_fragment,SenderFragment(), "historysender")
            .commit()
    }

}
