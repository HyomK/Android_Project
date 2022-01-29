package com.likefirst.btos.ui.archive

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentArchiveListBinding
import com.likefirst.btos.ui.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class ArchiveListFragment : BaseFragment<FragmentArchiveListBinding>(FragmentArchiveListBinding::inflate){

    override fun initAfterBinding() {
        binding.archiveListToolbar.archiveListPeriodIv.setOnClickListener {
            val periodDialog = ArchiveListPeriodDialog()
            periodDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.ArchiveDatePickerStyle)
            periodDialog.show(childFragmentManager, periodDialog.tag)
        }
    }
}