package com.likefirst.btos.ui.profile.setting

import android.annotation.SuppressLint
import android.view.View
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentSuggestBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import java.lang.Exception


class SuggestionFragment:BaseFragment<FragmentSuggestBinding>(FragmentSuggestBinding::inflate),MainActivity.onBackPressedListener{
    private var option = 0

    override fun initAfterBinding() {

        val menuItem = resources.getStringArray(R.array.suggest_item)
        val adapter= ArrayAdapter(requireContext(), R.layout.menu_dropdown_left_item, menuItem)
        binding.profileSuggestList.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.drop_menu_bg))
        binding.profileSuggestList.setAdapter(adapter)
        binding.profileSuggestList.dropDownHeight=300
        binding.profileSuggestToolbar.toolbarTitleTv.text="개발자에게 건의하기"

       binding.profileSuggestDoneBtn.setOnClickListener {
           sendEmail(option, binding.profileSuggestEdit.text.toString())
           requireActivity().supportFragmentManager.popBackStack()
       }
        binding.profileSuggestToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

       binding.profileSuggestList.setOnItemClickListener { adapterView, view, i, l ->
           option = i
       }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail(option:Int, content: String) {
        if(option ==-1){
            Toast.makeText(requireContext(),"카테고리를 선택해 주세요",Toast.LENGTH_SHORT).show()
            return
        }
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
        requireActivity().supportFragmentManager.popBackStack()
    }
}