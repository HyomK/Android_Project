package com.likefirst.btos.ui.home


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.likefirst.btos.R

import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryActivity
import java.time.LocalTime


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun initAfterBinding() {
        val animationView: LottieAnimationView = binding.lottieAnimation
        animationView.loop(true)
        animationView.playAnimation()

        val mActivity = activity as MainActivity



        binding.homeNotificationBtn.setOnClickListener {
            mActivity.notifyDrawerHandler()

        }

        binding.homeMailBtn.setOnClickListener {
            mActivity.ChangeFragment().moveFragment(R.id.home_mailbox_layout, MailboxFragment())
        }

        binding.homeWriteBtn.setOnClickListener {
            mActivity.startNextActivity(DiaryActivity::class.java)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        setWindowImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setWindowImage(){
        val current : LocalTime = LocalTime.now()
        val now = current.hour
        Log.d("window", now.toString())

        if(now in 6..11){
            binding.windowIv.setImageResource(R.drawable.window_morning)
        }else if(now in 12..18) {
            binding.windowIv.setImageResource(R.drawable.window_afternoon)
        }else{
            binding.windowIv.setImageResource(R.drawable.window_night)
        }
    }

}