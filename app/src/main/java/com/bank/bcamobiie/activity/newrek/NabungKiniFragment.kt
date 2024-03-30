package com.bank.bcamobiie.activity.newrek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bank.bcamobiie.databinding.FragmentNabungKiniBinding


class NabungKiniFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentNabungKiniBinding? = null
    private val binding : FragmentNabungKiniBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNabungKiniBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMulaiNewrek.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
    }

}