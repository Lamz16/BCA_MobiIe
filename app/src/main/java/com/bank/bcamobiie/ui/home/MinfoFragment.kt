package com.bank.bcamobiie.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.MutasiActivity
import com.bank.bcamobiie.adapter.MenuInHomeAdapter
import com.bank.bcamobiie.databinding.AlertMinfoSaldoBinding
import com.bank.bcamobiie.databinding.FragmentMinfoBinding
import com.bank.bcamobiie.datadummy.DataMenuInHome
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MinfoFragment : Fragment(), MenuInHomeAdapter.OnMenuClickListener {

    private var _binding: FragmentMinfoBinding? = null
    private val binding get() = _binding!!

    private var _alertSaldo: AlertMinfoSaldoBinding? = null
    private val alertSaldo: AlertMinfoSaldoBinding get() = _alertSaldo!!

    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob
    private val viewModel: InputDataViewModel by viewModel()
    private val firebaseViewModel: FirebaseDataViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        startIndicatorChangeJob()


        val recyclerView: RecyclerView = binding.rvMenuMinfo
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = MaterialDividerItemDecoration(requireActivity(), layoutManager.orientation).apply { isLastItemDecorated = false }
        itemDecoration.setDividerColorResource(requireContext(), R.color.color_line_divider)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MenuInHomeAdapter(DataMenuInHome.listMenuMinfo)
        adapter.setOnMenuItemClickListener(this)
        recyclerView.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        indicatorChangeJob?.cancel()
    }

    override fun onItemClick(menuId: Int) {
        when (menuId) {
            1 -> showAlertSaldo()
            2 -> startActivity(Intent(requireContext(), MutasiActivity::class.java))
        }
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignalMinfo.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

    private fun showAlertSaldo() {
        val alertBuilder = AlertDialog.Builder(requireContext())
        _alertSaldo = AlertMinfoSaldoBinding.inflate(layoutInflater)
        val view = alertSaldo.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(900, 1050)

        alertSaldo.tvDateSaldo.text = Utils.getCurrentDateTime()
        viewModel.getSession().observe(viewLifecycleOwner){
            firebaseViewModel.fetchSaldo(it.idKartu)
            firebaseViewModel.fetchRekening(it.idKartu)
        }


        firebaseViewModel.getSaldo().observe(viewLifecycleOwner) { saldo ->
            val nominalRupiah = saldo?.let { Utils.formatToRupiah(it.toLong()) }
            alertSaldo.textNominal.text = nominalRupiah
        }

        firebaseViewModel.getRekening().observe(viewLifecycleOwner) { rekening ->
            alertSaldo.tvNoRekening.text = rekening
        }

        alertSaldo.btnOkSaldo.setOnClickListener {
            dialog.dismiss()
        }
    }


}