package com.likefirst.btos.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieConfig
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity
import kotlin.math.log

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    override fun initAfterBinding() {
        val mButton = binding.mButton

        mButton.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fr_layout, HomeFragment())
                .commit()
         }

       }

    inner class changeFragment( targetFragment: Fragment?) {

        var targetFragment : Fragment? =targetFragment

        fun backHome(){ //targetFragment:  현재 Fragment
            supportFragmentManager
                .beginTransaction()
                .remove(targetFragment!!)
                .commit()
            supportFragmentManager .popBackStack()
        }

        fun moveFragment(layout: Int){ //targetFragment: 이동할 Fragment
            if(targetFragment!=null){
               supportFragmentManager.commit {
                    replace(layout, targetFragment!!).setReorderingAllowed(
                        true
                    )
                    addToBackStack("")
                }
            }else{
                Log.d("MainActivity","fail to move")
            }

        }
//        when (index) {
//            0->{
//                supportFragmentManager
//                    .beginTransaction()
//                    .remove(targetFragment!!)
//                    .commit()
//                supportFragmentManager .popBackStack()
//                }
//
//            1 -> {
//                supportFragmentManager.commit {
//                    replace(R.id.home_mailbox_layout, MailboxFragment()).setReorderingAllowed(
//                        true
//                    )
//                    addToBackStack("")
//                }
//            }
//            2 -> {
//                supportFragmentManager
//                    .beginTransaction()
//                    .remove(MailboxFragment())
//                    .commit()
//                supportFragmentManager .popBackStack()
//            }
//            3->{
//                supportFragmentManager.commit {
//                    replace(R.id.main_layout, ReportFragment()).setReorderingAllowed(
//                        true
//                    )
//                    addToBackStack("")
//                }
//            }
//        }
    }


}