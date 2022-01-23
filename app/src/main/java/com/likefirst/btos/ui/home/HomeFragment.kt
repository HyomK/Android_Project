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
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.plant.PlantItemFragment
import java.time.LocalTime


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
   var isMailboxOpen =false

    override fun initAfterBinding() {
        val animationView: LottieAnimationView = binding.lottieAnimation
        animationView.loop(true)
        animationView.playAnimation()

        val mActivity = activity as MainActivity

        binding.homeWriteBtn.isClickable = !mActivity.isDrawerOpen
        binding.homeNotificationBtn.setOnClickListener {
            mActivity.notifyDrawerHandler()

        }

        binding.homeMailBtn.setOnClickListener {
            isMailboxOpen = true

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
            mActivity.startNextActivity(DiaryActivity::class.java)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d("home","onhidden ${isHidden } mailopen ${isMailboxOpen }")
        val mActivity = activity as MainActivity
        if(isHidden || isMailboxOpen ){
            mActivity.isDrawerOpen=false
            if(isMailboxOpen){
                val item =  requireActivity().supportFragmentManager.fragments
                Log.d("homeSTACK","homeitem  ${item.toString()} }")
                requireActivity().supportFragmentManager.popBackStack()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .show(this)
                    .commit()
                isMailboxOpen=false
            }

        }else{
            mActivity.isDrawerOpen=true
            isMailboxOpen=false
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

        if(now in 6..18){
            binding.windowIv.setImageResource(R.drawable.window_morning)
        }else if(now in 18..20) {
            binding.windowIv.setImageResource(R.drawable.window_afternoon)
        }else{
            binding.windowIv.setImageResource(R.drawable.window_night)
        }
    }
}