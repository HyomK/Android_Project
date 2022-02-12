package com.likefirst.btos.ui.profile.setting

import android.view.View
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentSuggestBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService




class SuggestionFragment:BaseFragment<FragmentSuggestBinding>(FragmentSuggestBinding::inflate),MainActivity.onBackPressedListener{
    override fun initAfterBinding() {

        val menuItem = resources.getStringArray(R.array.suggest_item)
        val adapter= ArrayAdapter(requireContext(), R.layout.menu_dropdown_item, menuItem)
        binding.profileSuggestList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.profileSuggestList.setAdapter(adapter)
        binding.profileSuggestList.dropDownHeight=300
        binding.profileSuggestToolbar.toolbarTitleTv.text="개발자에게 건의하기"

       /* binding.profileSuggestEdit.isClickable=false
        binding.profileSuggestEdit.isFocusable=false*/

       binding.profileSuggestDoneBtn.setOnClickListener {
           requireActivity().supportFragmentManager.popBackStack()
       }
        binding.profileSuggestToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

       /*binding.profileSuggestList.setOnItemClickListener {
               adapterView, view, i, l ->
                when(i){
                    3->{
                        binding.profileSuggestEdit.isFocusable=true
                        binding.profileSuggestEdit.isFocusableInTouchMode=true
                    }
                }
       }*/

    }

    override fun onBackPressed() {
        val imm :InputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.profileSuggestDoneBtn.getWindowToken(), 0);
        requireActivity().supportFragmentManager.popBackStack()
    }
}