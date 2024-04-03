package com.bank.bcamobiie.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.databinding.ActivityMutasilistBinding
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MutasilistActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMutasilistBinding
    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    private val viewModel: InputDataViewModel by viewModel()
    private val firebaseViewModel: FirebaseDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutasilistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startIndicatorChangeJob()
        initialRek()

    }

    private fun initialRek(){
        viewModel.getSession().observe(this){
            firebaseViewModel.fetchRekening(it.idKartu)
        }

        firebaseViewModel.getRekening().observe(this) { rekening ->
            binding.noRek.text = formatAccountNumber(rekening)
        }

        val time = Utils.getCurrentDateTimeYear()
        binding.tglInquiry.text = time

        binding.jenisTransaksi.text = "Semua"
    }


    private fun formatAccountNumber(number: String): String {
        val formattedNumber = StringBuilder()

        formattedNumber.append(number.substring(0, 3))
        formattedNumber.append("-")
        formattedNumber.append(number.substring(3, 7))
        formattedNumber.append("-")
        formattedNumber.append(number.substring(7))

        return formattedNumber.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        indicatorChangeJob?.cancel()
        binding
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignalMutasi.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

}