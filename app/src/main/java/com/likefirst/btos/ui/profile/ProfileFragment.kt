package com.likefirst.btos.ui.profile

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.R
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.databinding.FragmentProfileBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.profile.plant.PlantFragment
import com.likefirst.btos.ui.profile.premium.PremiumFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.data.remote.plant.view.SharedSelectModel
import com.likefirst.btos.ui.profile.setting.NoticeActivity
import com.likefirst.btos.ui.profile.setting.SettingFragment
import com.likefirst.btos.ui.profile.setting.SuggestionFragment
import com.likefirst.btos.utils.LiveSharedPreferences

class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    var isSettingOpen = false
    var isFetch=true
    lateinit var  sharedSelectModel : SharedSelectModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedSelectModel= ViewModelProvider(requireActivity()).get(SharedSelectModel::class.java)
        sharedSelectModel.getLiveData().observe(viewLifecycleOwner, Observer<Bundle>{
            val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!

            binding.profileIv.setImageResource(requireContext()!!.resources.getIdentifier(
                plantName[it.getInt("plantIdx")-1]
                        +"_"+it.getInt("level").toString()
                        +"_circle","drawable",
                requireActivity().packageName))
            binding.profileLevelTv.text=it.getString("plantName")+" "+it.getInt("level").toString()+"단계"
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spf = requireActivity().getSharedPreferences("EditName",MODE_PRIVATE) // 기존에 있던 데이터
        val liveSharedPreference = LiveSharedPreferences(spf)
        liveSharedPreference
            .getString("UserName", "undefine")
            .observe(this, Observer<String> { result ->
                if(result!="undefine"){
                    binding.profileNicknameTv.text=result
                }
            })
    }


    override fun initAfterBinding() {
        initProfile()
        initClickListener()

    }

    fun initClickListener(){
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
//            requireActivity().supportFragmentManager
//                .beginTransaction()
//                .addToBackStack("profile-save")
//                .commit()
            val intent = Intent(requireActivity(),NoticeActivity::class.java)
            startActivity(intent)
        }
        binding.profileSuggestTv.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_layout, SuggestionFragment(),"suggestion")
                .addToBackStack("profile-save")
                .commit()
        }
    }

    fun initProfile(){
        val plantDB = PlantDatabase.getInstance(requireContext())!!
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        val plant =plantDB.plantDao().getSelectedPlant("selected")!!
        val plantName=requireContext()!!.resources.getStringArray(R.array.plantEng)!!
        val profile = requireContext()!!.resources.getIdentifier(
            plantName[plant.plantIdx-1]
                    +"_"+plant.currentLevel.toString()
                    +"_circle","drawable",
            requireActivity().packageName)
        binding.profileIv.setImageResource(profile)
        binding.profileNicknameTv.text=userDatabase.userDao().getNickName()!!
        binding.profileLevelTv.text=plant.plantName+" "+plant.currentLevel+"단계"
        if(plant.maxLevel!=plant.currentLevel)binding.profileMaxTv.visibility= View.GONE
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
            val fragments = arrayOf("premium", "plantrv","plantItem")
            cleanUpFragment(fragments)
            if (isSettingOpen) {
                val fragments = arrayOf("setName", "setBirth", "setFont", "setAppinfo")
                cleanUpFragment(fragments)
                isSettingOpen=false
            }
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager
                    .findFragmentByTag("setting")?.let { remove(it) }
            }

        }
    }

  fun updateProfile() {
        initProfile()
    }


}