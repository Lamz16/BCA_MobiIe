package com.bank.bcamobiie.activity.newrek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bank.bcamobiie.databinding.FragmentRegistrasiNewRekBinding

class RegistrasiNewRekFragment : Fragment() {

    private var _binding : FragmentRegistrasiNewRekBinding? = null
    private val binding : FragmentRegistrasiNewRekBinding  get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrasiNewRekBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}