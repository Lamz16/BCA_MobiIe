package com.bank.bcamobiie.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.AntarbankActivity
import com.bank.bcamobiie.activity.AntarrekeningActivity
import com.bank.bcamobiie.activity.TFAntarrekActivity
import com.bank.bcamobiie.adapter.MenuInHomeAdapter
import com.bank.bcamobiie.databinding.FragmentTransferBinding
import com.bank.bcamobiie.datadummy.DataMenuInHome
import com.bank.bcamobiie.utils.Utils
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TransferFragment : Fragment() , MenuInHomeAdapter.OnMenuClickListener {

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!

    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        val root: View = binding.root

        startIndicatorChangeJob()

        val recyclerView: RecyclerView = binding.rvMenuMtf
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = MaterialDividerItemDecoration(requireActivity(), layoutManager.orientation).apply { isLastItemDecorated = false }
        itemDecoration.setDividerColorResource(requireContext(), R.color.color_line_divider)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MenuInHomeAdapter(DataMenuInHome.listMenuTf)
        adapter.setOnMenuItemClickListener(this)
        recyclerView.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        indicatorChangeJob?.cancel()
        _binding = null
    }

    override fun onItemClick(menuId: Int) {
        when (menuId) {
            1 -> {
                startActivity(Intent(requireContext(), AntarrekeningActivity::class.java))
            }
            2 -> startActivity(Intent(requireContext(), AntarbankActivity::class.java))
            3 -> startActivity(Intent(requireContext(), TFAntarrekActivity::class.java))
        }
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (indicatorImages.indices).random()
                binding.indicatorSignalMinfo.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

}