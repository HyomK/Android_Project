package com.likefirst.btos.ui.history

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
//                requireActivity().supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.history_fragment,SenderFragment(), "historysender")
//                    .commit()
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
//                    binding.historyToolbar.historyBackIv.visibility = View.GONE
//                    binding.historyToolbar.historySearchIv.visibility = View.VISIBLE
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.history_fragment, DiaryFragment(), "historydiary")
                        .commit()
                }
                R.id.history_radiobutton_third-> {
//                    binding.historyToolbar.historyBackIv.visibility = View.GONE
//                    binding.historyToolbar.historySearchIv.visibility = View.VISIBLE
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.history_fragment, MailFragment(), "historymail")
                        .commit()
                }
            }
        }

    }

    fun setDisplay(){
        val spf= requireActivity().getSharedPreferences("HistoryBackPos", AppCompatActivity.MODE_PRIVATE)
        val backPos= spf.getString("backPos","historysender")
        Log.e("historyTag","backPos -> ${ backPos}")
        when(backPos){
            "historysender"->{
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,SenderFragment(), "historysender")
                    .commit()
                binding.historyRadiobuttonFirst.isChecked=true
            }
            "historydiary" -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,DiaryFragment(), "historydiary")
                    .commit()
                binding.historyRadiobuttonSecond.isChecked=true
            }
            "historymail" -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.history_fragment,MailFragment(), "historymail")
                    .commit()
                binding.historyRadiobuttonThird.isChecked=true
            }
            "historydetail" -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.history_fragment,SenderFragment(), "historysender")
                    .addToBackStack(null)
                    .replace(R.id.history_fragment,SenderDetailFragment(), "historydetail")
                    .commit()
            }
        }
    }


//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if(isAdded){
//            Log.e("historyTagShow","isSWHO")
//            val editor= requireActivity().getSharedPreferences("HistoryBackPos", AppCompatActivity.MODE_PRIVATE).edit()
//            editor.putString("backPos","historysender")
//            editor.commit()
//            setDisplay()
//        }
//    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(isAdded){
            val editor= requireActivity().getSharedPreferences("HistoryBackPos", AppCompatActivity.MODE_PRIVATE).edit()
            editor.putString("backPos","historysender")
            editor.commit()
            setDisplay()
        }
    }



    override fun onStart() {
        super.onStart()
        setDisplay()
    }

}
