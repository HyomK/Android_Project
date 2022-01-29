package com.likefirst.btos.ui.profile

import android.system.Os.remove
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentProfileBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.home.MailboxFragment
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.premium.PremiumFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.profile.setting.SettingFragment
import com.likefirst.btos.ui.profile.setting.SuggestionFragment

class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    var isSettingOpen = false
    override fun initAfterBinding() {

        val mActivity = activity as MainActivity

        binding.profileCd.setOnClickListener {
            mActivity.supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout,  PlantFragment(), "plantrv")
                .addToBackStack("profile-save")
                .commit()
        }

        binding.profilePremiumTv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, PremiumFragment(),"premium")
                .addToBackStack("profile-save")
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
        binding.profileNoticeTv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, NoticeFragment(),"notice")
                .addToBackStack("profile-save")
                .commit()
        }
        binding.profileSuggestTv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, SuggestionFragment(),"suggestion")
                .addToBackStack("profile-save")
                .commit()
        }

    }

    fun cleanUpFragment(  fragments: Array<String>){
        fragments.forEach { fragment ->
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager
                    .findFragmentByTag(fragment)?.let { remove(it) }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isHidden && isAdded) {
            val fragments = arrayOf("premium", "plantrv","plantItem","notice")

            cleanUpFragment(fragments)
            if (isSettingOpen) {
                val fragments = arrayOf("setName", "setBirth", "setFont", "setAppinfo", "setNotify")
                cleanUpFragment(fragments)
            }
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager
                    .findFragmentByTag("setting")?.let { remove(it) }
            }

        }
    }


}