package com.likefirst.btos.ui.main


import android.util.Log
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    override fun initAfterBinding() {



        binding.mainBnv.itemIconTintList = null

        ChangeFragment().moveFragment(R.id.fr_layout,PremiumFragment())


        val dataset = Array(1000) { i -> "Number of index: $i"  }
        val adapter= NotifyRVAdapter(dataset)

        adapter.setMyItemCLickLister(object:NotifyRVAdapter.NotifyItemClickListener{
            override fun onClickItem() {
                binding.mainLayout.closeDrawers()
                ChangeFragment().moveFragment(R.id.fr_layout, MailViewFragment())
            }
        })

        binding.sidebarNotifyRv.adapter=adapter

       }

    fun notifyDrawerHandler(){
        val stacks = supportFragmentManager.getFragments()
        Log.d("Stack", stacks.size.toString())
        if(stacks.size ==1 ){
            binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            binding.mainLayout.openDrawer((GravityCompat.START))
        }

        else{
            binding.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    inner class ChangeFragment() {

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



}