package com.likefirst.btos.ui.main


import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.archive.ArchiveFragment
import com.likefirst.btos.ui.profile.ProfileFragment
import com.likefirst.btos.ui.home.HomeFragment
import com.likefirst.btos.ui.home.MailViewFragment
import com.likefirst.btos.ui.profile.plant.PlantFragment

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val profileFragment= ProfileFragment()
    private val plantFragment=PlantFragment()
    var isDrawerOpen =true



    override fun initAfterBinding() {

        binding.mainBnv.itemIconTintList = null

        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_layout, homeFragment, "home")
            .setReorderingAllowed(true)
            .commitNowAllowingStateLoss()


        val dataset = Array(30) { i -> "Number of index: $i" }
        val adapter = NotifyRVAdapter(dataset)

        adapter.setMyItemCLickLister(object : NotifyRVAdapter.NotifyItemClickListener {
            override fun onClickItem() {
                binding.mainLayout.closeDrawers()
                ChangeFragment().moveFragment(R.id.fr_layout, MailViewFragment())
            }
        })

        binding.sidebarNotifyRv.adapter = adapter
        binding.mainBnv.setOnItemSelectedListener { it ->BottomNavView().onNavigationItemSelected(it) }
    }

    inner class BottomNavView :NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(it: MenuItem): Boolean {

            when (it.itemId) {
                R.id.homeFragment -> {
                    isDrawerOpen=true
                    if (homeFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .remove(profileFragment)
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .remove(profileFragment)
                            .add(R.id.fr_layout, homeFragment, "home")
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                    }

                    return true
                }

                R.id.archiveFragment -> {
                    isDrawerOpen=false
                    if (archiveFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .remove(profileFragment)
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(profileFragment)
                            .add(R.id.fr_layout, archiveFragment, "archive")
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                    }
                    return true
                }
                R.id.profileFragment -> {
                    isDrawerOpen=false
                    if (profileFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("profileClick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .add(R.id.fr_layout, profileFragment, "profile")
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("profileClick", "noadded")
                    }

                    return true
                }
            }
            return false
        }

       }

    }



    fun notifyDrawerHandler(){
        val stacks = supportFragmentManager.getFragments()
        if(stacks.size ==1 ){
            binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            binding.mainLayout.openDrawer((GravityCompat.START))
        }else{
            binding.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }

    inner class ChangeFragment {

       //현재 Fragment를 삭제합니다
        fun removeFragment( presFragment: Fragment? ){
            supportFragmentManager
                .beginTransaction()
                .remove( presFragment!!)
                .commit()
           // supportFragmentManager .popBackStack()
        }

        //pres -> target 으로 이동하고 backstack을 추가합니다
        fun moveFragment(layout: Int,  targetFragment: Fragment?){ //targetFragment: 이동할 Fragment
            supportFragmentManager.commit {
                replace(layout, targetFragment!!).setReorderingAllowed(true)
                addToBackStack("")
            }
        }

        //pres -> target 으로 이동합니다
        fun hideFragment(layout : Int, presFragment: Fragment? , targetFragment: Fragment?){
            supportFragmentManager.beginTransaction()
                .remove(presFragment!!)
                .addToBackStack("")
                .replace(layout ,targetFragment!!)
                .commit()
        }

    }

    override fun onBackPressed() {
        if(homeFragment.isVisible){
            super.onBackPressed()
        } else {
            if(homeFragment.isAdded){
                supportFragmentManager.beginTransaction()
                    .show(homeFragment)
                    .hide(profileFragment)
                    .hide(archiveFragment)
                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .add(R.id.fr_layout, homeFragment)
                    .commitNow()
            }
            binding.mainBnv.menu.findItem(R.id.homeFragment).isChecked = true
        }
    }





}