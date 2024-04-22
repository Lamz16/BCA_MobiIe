package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.bcamobiie.adapter.DaftarRekAdapter
import com.bank.bcamobiie.data.DataAkun
import com.bank.bcamobiie.data.DataDaftarRek
import com.bank.bcamobiie.data.DataRekening
import com.bank.bcamobiie.databinding.ActivityDaftarrekBinding
import com.bank.bcamobiie.ui.home.TransferFragment
import com.bank.bcamobiie.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DaftarrekActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDaftarrekBinding
    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob
    private lateinit var adapter: DaftarRekAdapter
    private val daftarRekList = mutableListOf<DataDaftarRek>()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarrekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startIndicatorChangeJob()

        val dataNoRek = intent.getStringExtra(DataDaftarRek)

        adapter = DaftarRekAdapter(daftarRekList)
        binding.rvDaftarRek.apply {
            layoutManager = LinearLayoutManager(this@DaftarrekActivity)
            setHasFixedSize(true)
            this.adapter = this@DaftarrekActivity.adapter
        }

        databaseReference = FirebaseDatabase.getInstance().reference

        fetchData(dataNoRek ?: "")
        Log.d(TAG, "onCreate: $dataNoRek")

        binding.btnSend.setOnClickListener {
            val intent = Intent(this@DaftarrekActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }

    private fun fetchData(dataNoRek: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.d("DaftarrekActivity", "Fetching data for dataNoRek: $dataNoRek")

            databaseReference.child("data_rekening").orderByChild("idRekening").equalTo(dataNoRek)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("DaftarrekActivity", "Data from data_rekening received: ${snapshot.value}")

                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val rekening = data.getValue(DataRekening::class.java)
                                rekening?.let {
                                    val noRek = it.idRekening ?: ""
                                    val idKartu = it.idKartu ?: ""
                                    Log.d("DaftarrekActivity", "noRek: $noRek, idKartu: $idKartu")

                                    if (idKartu.isNotEmpty()) {
                                        fetchAkunData(idKartu, noRek)
                                    } else {
                                        Log.w("DaftarrekActivity", "idKartu is empty after fetching")
                                    }
                                }
                            }
                        } else {
                            Log.w("DaftarrekActivity", "No data found in data_rekening for idRekening: $dataNoRek")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("DaftarrekActivity", "Failed to read value from data_rekening.", error.toException())
                    }
                })

        } catch (e: Exception) {
            Log.e("DaftarrekActivity", "Error fetching data", e)
        }
    }


    private fun fetchAkunData(idKartu: String, noRek: String) {
        databaseReference.child("data_akun").orderByChild("idKartu").equalTo(idKartu)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val akun = data.getValue(DataAkun::class.java)
                        akun?.let {
                            val nama = it.nama ?: ""
                            val daftarRek = DataDaftarRek(nama, noRek)
                            daftarRekList.add(daftarRek)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DaftarrekActivity", "Failed to read value from data_akun.", error.toException())
                }
            })
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
        const val TAG : String = "DAFTAR REK ACTIVITY"
        const val DataDaftarRek : String = "Data_Daftar_Rekening"
    }

}