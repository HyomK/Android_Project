package com.likefirst.btos.ui.profile.setting

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentSetNotificationBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity

class SetNotificationFragment:BaseFragment<FragmentSetNotificationBinding>(FragmentSetNotificationBinding::inflate),MainActivity.onBackPressedListener {
    override fun initAfterBinding() {
        val arraylist = arrayListOf<String>("user1","user2","user3")
        val adapter = BlackListRVAdapter(arraylist)
        adapter.setOnListener(object:BlackListRVAdapter.BlackListListener{
            override fun onClick(userName : String) {
                super.onClick(userName)
                //api user이름 전달
                Log.d("username",userName)
            }
            override fun removeUser(pos: Int) {

            }
        })
        binding.setNotifyRv.adapter=adapter

        var btn1 = true
        binding.setNotifyBtn.setOnClickListener {
            btn1= pushToggleSwitcher( btn1 ,binding.setNotifyBtn,  binding.setNotifyToggleIv)
        }

        var btn2 = true
        binding.setNotifyAgeBtn.setOnClickListener {
            btn2= pushToggleSwitcher( btn2 ,binding.setNotifyAgeBtn,  binding.setNotifyAgeToggleIv)
        }

        binding.setNotifyToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    fun pushToggleSwitcher(status: Boolean , btn : ImageView, bg:ImageView):Boolean {
        if (status) {
            btn.setImageResource(R.drawable.ic_toggle_false)
            bg.visibility = View.INVISIBLE
        } else {
            bg.visibility= View.VISIBLE
           // bg.setImageResource(R.drawable.select_toggle)
            btn.setImageResource(R.drawable.ic_toggle_true)
        }
        return !status

    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

}