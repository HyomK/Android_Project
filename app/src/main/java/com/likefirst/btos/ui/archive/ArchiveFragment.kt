package com.likefirst.btos.ui.archive

import androidx.recyclerview.widget.RecyclerView
import com.likefirst.btos.databinding.FragmentArchiveBinding
import com.likefirst.btos.ui.BaseFragment

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(FragmentArchiveBinding::inflate) {
    override fun initAfterBinding() {
        val archiveAdapter = ArchiveFragmentVPAdapter(this)
        binding.archiveVp.apply {
            adapter = archiveAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

}