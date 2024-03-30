package com.bank.bcamobiie.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bank.bcamobiie.databinding.ActivityAntarrekeningBinding

class AntarrekeningActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAntarrekeningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAntarrekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
}