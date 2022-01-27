package com.likefirst.btos.ui.profile

import android.system.Os.remove
import android.util.Log
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentProfileBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.home.MailboxFragment
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.premium.PremiumFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.profile.setting.SettingFragment

class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    var isSettingOpen = false
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity

        binding.profileCd.setOnClickListener {
            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fr_layout,  PlantFragment(), "profile")
                .addToBackStack(null)
                .commit()
            //mActivity.ChangeFragment().hideFragment(R.id.fr_layout,this, PlantFragment())
        }

        binding.profilePremiumTv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, PremiumFragment(),"premium")
                .show(PremiumFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.profileSettingTv.setOnClickListener {
           isSettingOpen=true
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, SettingFragment(),"setting")
                .show(SettingFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)


        if(isHidden &&isAdded){
            Log.d("premium","hidden")
            requireActivity().supportFragmentManager.commit{
                requireActivity().supportFragmentManager
                    .findFragmentByTag("premium")?.let { remove(it) }

            }
            if(isSettingOpen){
                val fragments = arrayOf("setName","setBirth","setFont","setAppinfo","setNotify")
                fragments.forEach { fragment ->
                    requireActivity().supportFragmentManager.commit{
                        requireActivity().supportFragmentManager
                            .findFragmentByTag(fragment)?.let { remove(it) }
                    }
                }
            }

            requireActivity().supportFragmentManager.commit{
                requireActivity().supportFragmentManager
                    .findFragmentByTag("setting")?.let { remove(it) }
            }

        }
    }


}