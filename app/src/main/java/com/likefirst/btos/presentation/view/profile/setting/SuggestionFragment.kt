package com.likefirst.btos.presentation.view.profile.setting

import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentSuggestBinding
import com.likefirst.btos.presentation.BaseFragment
import com.likefirst.btos.presentation.view.main.MainActivity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.view.inputmethod.InputMethodManager

import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


class SuggestionFragment:BaseFragment<FragmentSuggestBinding>(FragmentSuggestBinding::inflate),MainActivity.onBackPressedListener{
    private var option = -1

    override fun initAfterBinding() {

        val menuItem = resources.getStringArray(R.array.suggest_item)
        val adapter= ArrayAdapter(requireContext(), R.layout.menu_dropdown_left_item, menuItem)
        binding.profileSuggestList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.profileSuggestList.setAdapter(adapter)
        binding.profileSuggestToolbar.toolbarTitleTv.text="개발자에게 건의하기"

       binding.profileSuggestDoneBtn.setOnClickListener {
           if(option ==-1){
               Snackbar.make(requireView(),"카테고리를 선택해 주세요",Snackbar.LENGTH_SHORT).show()
           }else{
               sendEmail(option, binding.profileSuggestEdit.text.toString())
               requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
           }
       }
        binding.profileSuggestToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

       binding.profileSuggestList.setOnItemClickListener { adapterView, view, i, l ->
           option = i
       }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail(option:Int, content: String) {

        val menuItem = resources.getStringArray(R.array.suggest_item)
        val emailAddress = "beyondtheotherside00@gmail.com"
        val title = "건의사항 [${menuItem[option]}]"
        val emailIntent = Intent(Intent.ACTION_SEND)
        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress ))
            emailIntent.type = "text/html"
            emailIntent.setPackage("com.google.android.gm")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,title);
            emailIntent.putExtra(Intent.EXTRA_TEXT,content)
            if (emailIntent.resolveActivity(requireActivity().packageManager) != null) startActivity(emailIntent)
            startActivity(emailIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf( emailAddress ))
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
    }
    override fun onBackPressed() {
        val imm :InputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.profileSuggestDoneBtn.getWindowToken(), 0);
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.popBackStack()
    }

}