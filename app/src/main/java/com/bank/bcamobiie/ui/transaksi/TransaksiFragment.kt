package com.bank.bcamobiie.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.FragmentTransaksiBinding

class TransaksiFragment : Fragment() {

    private lateinit var binding : FragmentTransaksiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTransaksiBinding.inflate(inflater, container, false)

        return binding.root
    }

}