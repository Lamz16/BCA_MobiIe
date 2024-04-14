package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.databinding.ActivityAntarrekeningBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AntarrekeningActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAntarrekeningBinding
    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAntarrekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startIndicatorChangeJob()

        binding.apply {

            btnSend.setOnClickListener {
                val dataNoRek = inputRek1.text.toString()
                val intent = Intent(this@AntarrekeningActivity, DaftarrekActivity::class.java)
                intent.putExtra(DaftarrekActivity.DataDaftarRek, dataNoRek)
                startActivity(intent)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        indicatorChangeJob?.cancel()
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (indicatorImages.indices).random()
                binding.indicatorSignalArek.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }
    
    companion object {
        const val TAG = "Antar Rekening Activity"
    }

}