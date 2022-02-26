package com.likefirst.btos.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityTutorialBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity

class TutorialActivity : BaseActivity<ActivityTutorialBinding>(ActivityTutorialBinding::inflate) {

    private lateinit var viewpager : ViewPager2
    private var backpressedTime : Long = 0

    override fun initAfterBinding() {

        val intent = intent
        val nickname = intent.getStringExtra("nickname")
        binding.tutorialNameTv.text = "반가워요, ${nickname}님."
        binding.tutorialNameTv.bringToFront()
        binding.tutorialNameCl.bringToFront()
        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        // animation_logo_FadeOut
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tutorialNameCl.startAnimation(animFadeOut)
            binding.tutorialNameTv.startAnimation(animFadeOut)
            binding.tutorialVp.startAnimation(animFadeIn)
            binding.tutorialTablayout.startAnimation(animFadeIn)
            initViewPager()
        },2000)

    }

    private fun initViewPager() {

        val imageList = resources.getStringArray(R.array.tutorial_lottie)
        val introTextList = resources.getStringArray(R.array.tutorial)
        val imageAdapter = TutorialViewPagerAdapter(this,imageList,introTextList)

        viewpager = binding.tutorialVp
        viewpager.adapter = imageAdapter
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager.overScrollMode = View.OVER_SCROLL_NEVER

        val tablayout = binding.tutorialTablayout
        TabLayoutMediator(tablayout, viewpager) { tab, position ->
        }.attach()

        imageAdapter.setOnItemClickListener(object: TutorialViewPagerAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                val intent = Intent(this@TutorialActivity,MainActivity::class.java)
                intent.putExtra("isNewUser",true)
                finish()
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        Log.e("TUTORIAL",(System.currentTimeMillis() - backpressedTime).toString())
        if(System.currentTimeMillis() - backpressedTime < 2000) {
            super.onBackPressed()
        }else{
            Toast.makeText(this,"뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            backpressedTime = System.currentTimeMillis()
            return
        }
    }
}