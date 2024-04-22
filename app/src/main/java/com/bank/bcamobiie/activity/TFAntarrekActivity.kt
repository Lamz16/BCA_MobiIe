package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.data.DataChoiceRek
import com.bank.bcamobiie.databinding.ActivityTfantarrekBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TFAntarrekActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTfantarrekBinding
    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob
    private lateinit var choiceRek : DataChoiceRek

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfantarrekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startIndicatorChangeJob()

        binding.getNorek.setOnClickListener {
            startActivity(Intent(this, ChoicerekActivity::class.java))
        }

        val dataUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KE_REK, DataChoiceRek::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KE_REK)
        }

        if (dataUser!= null){
            choiceRek = dataUser
            val textRek = "${choiceRek.noRek} - ${choiceRek.nama}"
            binding.choiceRek.text =textRek
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
        const val KE_REK = "ke rekening"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }


}