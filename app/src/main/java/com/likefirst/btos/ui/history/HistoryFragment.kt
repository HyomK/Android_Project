package com.likefirst.btos.ui.history

import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHistoryBinding
import com.likefirst.btos.ui.BaseFragment

class HistoryFragment: BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    override fun initAfterBinding() {

        binding.historyRadiobuttonFirst.isChecked = true

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
            .add(R.id.history_fragment,SenderFragment(), "historysender")
            .show(SenderFragment())
            .commit()
    }
}
