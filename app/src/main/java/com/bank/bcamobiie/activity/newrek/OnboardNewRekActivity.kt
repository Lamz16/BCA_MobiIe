package com.bank.bcamobiie.activity.newrek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bank.bcamobiie.databinding.ActivityOnboardNewRekBinding

class OnboardNewRekActivity : AppCompatActivity() {
    private var _binding : ActivityOnboardNewRekBinding? = null
    private val binding  : ActivityOnboardNewRekBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardNewRekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            backOnboard.setOnClickListener {
                finish()
            }

            button.setOnClickListener {
                val intent = Intent(this@OnboardNewRekActivity, NewRekActivity::class.java)
                startActivity(intent)

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
       finish()
    }



}