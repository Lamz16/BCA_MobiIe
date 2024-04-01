package com.bank.bcamobiie.ui.home

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.BcakeyboardActivity
import com.bank.bcamobiie.activity.CardlessActivity
import com.bank.bcamobiie.activity.FlazzmenuActivity
import com.bank.bcamobiie.activity.WelcomeActivity
import com.bank.bcamobiie.adapter.MenuAdapter
import com.bank.bcamobiie.databinding.AlertFlazzBinding
import com.bank.bcamobiie.databinding.AlertinfologoutBinding
import com.bank.bcamobiie.databinding.FragmentHomeBinding
import com.bank.bcamobiie.datadummy.dataMenu
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), MenuAdapter.OnMenuItemClickListener{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var _alertLogout: AlertinfologoutBinding? = null
    private val alertLogout: AlertinfologoutBinding get() = _alertLogout!!

    private var _alertFlazzBinding: AlertFlazzBinding? = null
    private val alertFlazzBinding: AlertFlazzBinding get() = _alertFlazzBinding!!

    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    private val viewModel: InputDataViewModel by viewModel()
    private val firebaseViewModel: FirebaseDataViewModel by viewModel()
    private var currentNamaNasabah: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerMenu
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        val adapter = MenuAdapter(dataMenu)
        adapter.setOnMenuItemClickListener(this)
        recyclerView.adapter = adapter

        startIndicatorChangeJob()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner){ dataUser ->
            firebaseViewModel.getUserData().observe(viewLifecycleOwner) { namaNasabah ->
                    currentNamaNasabah = namaNasabah
                binding.tvNameNasabah.text = currentNamaNasabah
            }
            firebaseViewModel.fetchUserData(dataUser.idKartu)
        }


        binding.btnLogout.setOnClickListener {
            showAlertlogout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        indicatorChangeJob?.cancel()
    }


    override fun onItemClick(menuId: Int) {
        when(menuId){
            1 -> findNavController().navigate(R.id.toMenu1)

            2 -> findNavController().navigate(R.id.toMenu2)

            3 -> findNavController().navigate(R.id.toMenu3)

            4 -> findNavController().navigate(R.id.toMenu4)

            5 -> startActivity(Intent(requireContext(), CardlessActivity::class.java))

            6 -> findNavController().navigate(R.id.toMenu5)

            7 -> startActivity(Intent(requireContext(), BcakeyboardActivity::class.java))

            8 -> checkNfcStatus()

        }
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignal.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

    private fun showAlertlogout() {
        val alertBuilder = AlertDialog.Builder(requireContext())
        _alertLogout= AlertinfologoutBinding.inflate(layoutInflater)
        val view = alertLogout.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(900, 1050)
        alertLogout.apply {
            btnCancelLogout.setOnClickListener {
                dialog.dismiss()
            }
            btnLogout.setOnClickListener {
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }

    }

    private fun checkNfcStatus() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            showNfcDisabledDialog()
        }else{
            startActivity(Intent(requireContext(), FlazzmenuActivity::class.java))
        }
    }

    private fun showNfcDisabledDialog() {
        val alertBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.RoundedMaterialDialog)
        _alertFlazzBinding = AlertFlazzBinding.inflate(layoutInflater)
        val view = alertFlazzBinding.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        alertFlazzBinding.apply {
            cancelNfc.setOnClickListener {
                dialog.dismiss()
            }
            settingsNfc.setOnClickListener {
                val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                startActivity(intent)
            }
        }
    }

}