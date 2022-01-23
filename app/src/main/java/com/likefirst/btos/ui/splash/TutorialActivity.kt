package com.likefirst.btos.ui.splash

import android.content.Intent
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityTutorialBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity

class TutorialActivity : BaseActivity<ActivityTutorialBinding>(ActivityTutorialBinding::inflate) {

    private lateinit var viewpager : ViewPager2

    override fun initAfterBinding() {
        initViewPager()
    }

    private fun initViewPager() {

        val imageList = ArrayList<Int>()
        imageList.add(R.drawable.ic_bg_received)
        imageList.add(R.drawable.ic_bg_sent)
        imageList.add(R.drawable.ic_bg_diary)
        val imageAdapter = TutorialViewPagerAdapter(this,imageList)

        viewpager = binding.tutorialVp
        viewpager.adapter = imageAdapter
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val tablayout = binding.tutorialTablayout
        TabLayoutMediator(tablayout, viewpager) { tab, position ->
        }.attach()

        imageAdapter.setOnItemClickListener(object: TutorialViewPagerAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                finish()
                startActivity(Intent(this@TutorialActivity,MainActivity::class.java))
            }
        })
    }
}