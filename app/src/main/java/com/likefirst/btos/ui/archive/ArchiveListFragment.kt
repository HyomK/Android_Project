package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.util.Log
import com.likefirst.btos.databinding.FragmentArchiveListBinding
import com.likefirst.btos.ui.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class ArchiveListFragment : BaseFragment<FragmentArchiveListBinding>(FragmentArchiveListBinding::inflate){

    override fun initAfterBinding() {
        val from = "2018-09-06 11:11:11"
        val fm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        val to: Date = fm.parse(from)!!

        Log.d("to", to.toString())
    }
}