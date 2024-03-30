package com.bank.bcamobiie.activity.menucardless

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.adapter.AdapterMenuCardless
import com.bank.bcamobiie.adapter.MenuAdapter
import com.bank.bcamobiie.databinding.FragmentCardlessMenuBinding
import com.bank.bcamobiie.databinding.FragmentHomeBinding
import com.bank.bcamobiie.databinding.MenuItemCardlessBinding
import com.bank.bcamobiie.datadummy.dataMenu
import com.bank.bcamobiie.datadummy.dataMenuCardless


class CardlessMenuFragment : Fragment(), AdapterMenuCardless.OnMenuItemClickListener {

    private var _binding : FragmentCardlessMenuBinding? = null
    private val binding : FragmentCardlessMenuBinding get() =  _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCardlessMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.menuCardless
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = AdapterMenuCardless(dataMenuCardless)
        adapter.setOnMenuItemClickListener(this)
        recyclerView.adapter = adapter

        return root

    }

    override fun onItemClick(menuId: Int) {

    }

}