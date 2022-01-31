package com.likefirst.btos.ui.home


import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import com.airbnb.lottie.LottieAnimationView
import com.likefirst.btos.R

import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryActivity
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.plant.PlantItemFragment
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.getLastPostingDate
import java.time.LocalTime
import java.util.*
import kotlin.time.Duration.Companion.milliseconds


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
   var isMailboxOpen =false

    override fun initAfterBinding() {

        val mActivity = activity as MainActivity

        initFlowerPot()
      
        binding.homeNotificationBtn.setOnClickListener {
            if(!mActivity.mailOpenStatus())mActivity.notifyDrawerHandler("open")

        }

        binding.homeMailBtn.setOnClickListener {

            mActivity.isMailOpen = true
            mActivity.notifyDrawerHandler("lock")

            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.home_mailbox_layout, MailboxFragment(), "mailbox")
                .addToBackStack(null)
                .show(MailboxFragment())
                .commit()
            val item =  requireActivity().supportFragmentManager.fragments
            Log.d("homeSTACK","homeitem  ${item.toString()} }")
        }

        binding.homeWriteBtn.setOnClickListener {
            val date = dateToString(Date())
            val intent = Intent(requireContext(), DiaryActivity::class.java)
            intent.putExtra("diaryDate", date)
            startActivity(intent)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d("home","onhidden ${isHidden } mailopen ${isMailboxOpen }")
        val mActivity = activity as MainActivity

        if(isHidden || mActivity.isMailOpen ){
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager.findFragmentByTag("mailbox")?.let { remove(it) }
                requireActivity().supportFragmentManager.findFragmentByTag("writemail")?.let { remove(it) }
                requireActivity().supportFragmentManager.findFragmentByTag("viewmail")?.let { remove(it) }
            }
            mActivity.isMailOpen=false
            mActivity.notifyDrawerHandler("lock")
        }else{
            mActivity.notifyDrawerHandler("unlock")
            binding.homeNotificationBtn.isClickable =true

        }
    }

    fun initFlowerPot(){
        val animationView: LottieAnimationView = binding.lottieAnimation
        val lastPostingDate = getLastPostingDate()
        Log.d("lastPostingDate", lastPostingDate.toString())
        val mCalendar = GregorianCalendar.getInstance()
        val currentMillis = mCalendar.timeInMillis
        val lastMillis = mCalendar.timeInMillis
        val diffPostingDate = (currentMillis - lastMillis) / 1000 / (24*60*60)
        if (diffPostingDate >= 5){
            animationView.setAnimation("alocasia_sad_3.json")
        } else {
            animationView.setAnimation("alocasia_3.json")
        }
        animationView.loop(true)
        animationView.playAnimation()
        // TODO: 서버 반영해서 유저가 선택한 화분에 따라서 표시되게 변경, 현재는 더미데이터일 뿐임
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        setWindowImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val mActivity = activity as MainActivity
        setWindowImage()
        binding.homeNotificationBtn.isClickable =true
        isMailboxOpen=false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setWindowImage(){
        val current : LocalTime = LocalTime.now()
        val now = current.hour
        Log.d("window", now.toString())

        if(now in 6..18){
            binding.windowIv.setImageResource(R.drawable.window_morning)
        }else if(now in 18..20) {
            binding.windowIv.setImageResource(R.drawable.window_afternoon)
        }else{
            binding.windowIv.setImageResource(R.drawable.window_night)
        }
    }
}