package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.bcamobiie.adapter.DataTermAdapter
import com.bank.bcamobiie.databinding.ActivityKetentuanBinding
import com.bank.bcamobiie.datadummy.DataDummyTerm

class KetentuanActivity : AppCompatActivity() {

    private var _binding: ActivityKetentuanBinding? = null
    private val binding: ActivityKetentuanBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityKetentuanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val dataTermList = DataDummyTerm.dataTeksA
        val recyclerView = binding.rvDefinisi
        val adapter = DataTermAdapter(dataTermList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.apply {
            btnReject.setOnClickListener {
                finish()
            }

            btnAccept.setOnClickListener {
                val intent = Intent(this@KetentuanActivity, InputCardActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}