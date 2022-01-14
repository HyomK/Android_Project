package com.likefirst.btos.ui.main

import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initAfterBinding() {
        menuView()
    }

    private fun menuView() {
        val menuview = binding.mainBnv as BottomNavigationMenuView
        try{
            for (i in 0 until menuview.childCount){
                val menuItemView = menuview.getChildAt(i) as BottomNavigationItemView
                val title = menuItemView.findViewById(R.id.smallLabel) as TextView
                title.visibility = View.INVISIBLE
            }
        }catch(e: NoSuchFieldException) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
        }
    }
}