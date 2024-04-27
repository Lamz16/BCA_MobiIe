package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.bcamobiie.R
import com.bank.bcamobiie.adapter.ChoiceRekAdapter
import com.bank.bcamobiie.data.DataAkun
import com.bank.bcamobiie.data.DataChoiceRek
import com.bank.bcamobiie.data.DataRekening
import com.bank.bcamobiie.databinding.ActivityChoicerekBinding
import com.bank.bcamobiie.utils.Utils
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChoicerekActivity : AppCompatActivity(), ChoiceRekAdapter.OnItemClickListener {

    private lateinit var binding: ActivityChoicerekBinding
    private lateinit var akunListener: ValueEventListener
    private lateinit var rekeningListener: ValueEventListener
    private lateinit var database: DatabaseReference
    private lateinit var adapter: ChoiceRekAdapter
    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoicerekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startIndicatorChangeJob()

        database =
            FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference

        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        lifecycleScope.launch(Dispatchers.IO) {
            akunListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listDataAkun = mutableListOf<DataAkun>()
                    for (dataSnapShoot in snapshot.children) {
                        val dataAkun = dataSnapShoot.getValue(DataAkun::class.java)
                        dataAkun?.let { listDataAkun.add(it) }
                    }

                    rekeningListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val listDataRek = mutableListOf<DataRekening>()
                            for (dataSnapshot in snapshot.children) {
                                val dataRekening = dataSnapshot.getValue(DataRekening::class.java)
                                dataRekening?.let { listDataRek.add(it) }
                            }

                            val showDataList = mutableListOf<DataChoiceRek>()

                            for (dataAkun in listDataAkun) {
                                for (dataRekening in listDataRek) {
                                    if (dataAkun.idKartu == dataRekening.idKartu) {
                                        val showData = DataChoiceRek(
                                            dataAkun.nama,
                                            dataRekening.idRekening
                                        )
                                        showDataList.add(showData)
                                        break
                                    }
                                }
                            }
                            adapter = ChoiceRekAdapter(showDataList, this@ChoicerekActivity)
                            binding.rvListRek.adapter = adapter
                            val layoutManager = LinearLayoutManager(this@ChoicerekActivity)
                            val itemDecoration = MaterialDividerItemDecoration(this@ChoicerekActivity, layoutManager.orientation).apply { isLastItemDecorated = false }
                            itemDecoration.setDividerColorResource(this@ChoicerekActivity, R.color.color_line_divider3)
                            binding.rvListRek.addItemDecoration(itemDecoration)
                            binding.rvListRek.layoutManager = LinearLayoutManager(this@ChoicerekActivity)
                            Log.d("ChoiceRekActivity", "Data Rekening: $listDataRek")
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    }
                    database.child("data_rekening").addListenerForSingleValueEvent(rekeningListener)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            database.child("data_akun").addListenerForSingleValueEvent(akunListener)
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

    override fun onItemClick(dataChoiceRek: DataChoiceRek) {
        val intent = Intent(this, TFAntarrekActivity::class.java)
        intent.putExtra(TFAntarrekActivity.KE_REK, dataChoiceRek)
        startActivity(intent)
        finish()
    }

}
