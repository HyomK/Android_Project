package com.likefirst.btos.ui.main


import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.airbnb.lottie.LottieAnimationView
import com.likefirst.btos.R

import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun initAfterBinding() {
        val animationView: LottieAnimationView = binding.lottieAnimation
        animationView.loop(true)
        animationView.playAnimation()

        val mActivity = activity as MainActivity
        binding.homeDrawerLayout.closeDrawers()

        binding.homeNotificationBtn.setOnClickListener {
            binding.homeDrawerLayout.openDrawer((GravityCompat.START))

        }
        binding.sidebarExitBtn.setOnClickListener {
            binding.homeDrawerLayout.closeDrawers()
        }

        binding.homeMailBtn.setOnClickListener {
            mActivity.changeFragment(MailboxFragment()).moveFragment(R.id.home_mailbox_layout)
        }

        binding.homeWriteBtn.setOnClickListener {
            mActivity.changeFragment(MailViewFragment()).moveFragment(R.id.home_main_layout)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        //setWindowImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setWindowImage(){
        val current : LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("h")
        val formatted = current.format(formatter)
        val now = formatted.toInt()


        if(now<=5){
            binding.windowIv.setImageResource(R.mipmap.window_morning)
        }else if(now >=12 && now < 18) {
            binding.windowIv.setImageResource(R.mipmap.window_afternoon)
        }else{
            binding.windowIv.setImageResource(R.mipmap.window_night)
        }
    }

}