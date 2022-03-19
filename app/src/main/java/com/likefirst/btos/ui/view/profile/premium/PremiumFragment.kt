package com.likefirst.btos.ui.view.profile.premium

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentPremiumBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.MainActivity


class PremiumFragment : BaseFragment <FragmentPremiumBinding>(FragmentPremiumBinding :: inflate),MainActivity.onBackPressedListener {



    override fun initAfterBinding() {

        val mActivity = activity as MainActivity
        binding.premiumToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
        val data = arrayListOf(R.drawable.alocasia_no_result)
        val adapter = PremiumVPAdapter(data)
        val currentVisibleItemPx = requireContext().dp2px(40f).toInt()
        binding.premiumVp.clipToPadding = false
        binding.premiumVp.setPadding(20, 0, 20, 0)
        binding.premiumVp.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = currentVisibleItemPx
                outRect.left = currentVisibleItemPx
            }
        })


        val nextVisibleItemPx = requireContext().dp2px(20f).toInt()
        val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx

        binding.premiumVp.offscreenPageLimit = 1

        binding.premiumVp.setPageTransformer { page, position ->
            page.translationX = -pageTranslationX * (position)
        }
        binding.premiumVp.adapter = adapter

        TabLayoutMediator(binding.premiumTablayout, binding.premiumVp) { tab, position -> }.attach()

    }


    fun Context.dp2px(@FloatRange(from = 0.0) dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }


    override fun onBackPressed() {
       // requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        val mainActivity = activity as MainActivity
        mainActivity.onBottomNavHandler("premium",R.id.homeFragment)
    }

}