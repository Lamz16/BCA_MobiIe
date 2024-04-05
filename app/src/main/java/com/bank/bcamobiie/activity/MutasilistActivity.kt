package com.bank.bcamobiie.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.bcamobiie.R
import com.bank.bcamobiie.adapter.MutasiAdapter
import com.bank.bcamobiie.data.DataTransaksi
import com.bank.bcamobiie.databinding.ActivityMutasilistBinding
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MutasilistActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMutasilistBinding
    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    private val viewModel: InputDataViewModel by viewModel()
    private val firebaseViewModel: FirebaseDataViewModel by viewModel()
    private lateinit var database: DatabaseReference
    private lateinit var adapter: MutasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutasilistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startIndicatorChangeJob()
        initialRek()
        database = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference

        adapter = MutasiAdapter(emptyList())
        binding.rvMutasi.layoutManager = LinearLayoutManager(this)
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = MaterialDividerItemDecoration(this, layoutManager.orientation).apply { isLastItemDecorated = false }
        itemDecoration.setDividerColorResource(this, R.color.color_line_divider2)
        binding.rvMutasi.addItemDecoration(itemDecoration)
        binding.rvMutasi.adapter = adapter
        fetchDataFromFirebase()

    }

    private fun initialRek(){
        viewModel.getSession().observe(this){
            firebaseViewModel.fetchRekening(it.idKartu)
        }

        firebaseViewModel.getRekening().observe(this) { rekening ->
            binding.noRek.text = formatAccountNumber(rekening)
        }
        val timeFrom = intent.getStringExtra(DATE_FROM)
        val timeTo = intent.getStringExtra(DATE_TO)

        val periodeText = getString(R.string.periode_placeholder, timeFrom, timeTo)
        binding.periode.text = periodeText

        val time = Utils.getCurrentDateTimeYear()
        binding.tglInquiry.text = time
        val jenisTran = intent.getStringExtra(DATA_MUTASI)

        binding.jenisTransaksi.text = jenisTran

        val searchEditText = binding.search
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filterData(s.toString())
            }
        })

    }


    private fun filterData(query: String) {
        val filteredTransactions = mutableListOf<DataTransaksi>()
        val jenisTran = intent.getStringExtra(DATA_MUTASI)
        val timeFrom = intent.getStringExtra(DATE_FROM)
        val timeTo = intent.getStringExtra(DATE_TO)

        viewModel.getSession().observe(this){
            firebaseViewModel.fetchRekening(it.idKartu)
        }

        firebaseViewModel.getRekening().observe(this) { idRekening ->
            val queryRef = database.child("data_transaksi").orderByChild("idRekening").equalTo(idRekening)
            queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    filteredTransactions.clear() // Clear the list before filtering
                    for (dataSnapshot in snapshot.children) {
                        val transaction = dataSnapshot.getValue(DataTransaksi::class.java)
                        transaction?.let {
                            val transactionDate = it.tglTran
                            val startDateObj = parseDate(timeFrom!!)
                            val endDateObj = parseDate(timeTo!!)
                            val transactionDateObj = parseDate(transactionDate!!)

                            if (isDateWithinRange(transactionDateObj, startDateObj, endDateObj)) {
                                when (jenisTran) {
                                    "Uang Masuk" -> {
                                        if (it.jenisTran == "CR" && it.contains(query)) {
                                            filteredTransactions.add(it)
                                        }
                                    }
                                    "Uang Keluar" -> {
                                        if (it.jenisTran == "DB" && it.contains(query)) {
                                            filteredTransactions.add(it)
                                        }
                                    }
                                    else -> {
                                        if (it.contains(query)) {
                                            filteredTransactions.add(it)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    adapter.setData(filteredTransactions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MutasiActivity", "Failed to read value.", error.toException())
                }
            })
        }
    }


    private fun fetchDataFromFirebase() {

        val jenisTran = intent.getStringExtra(DATA_MUTASI)
        val timeFrom = intent.getStringExtra(DATE_FROM)
        val timeTo = intent.getStringExtra(DATE_TO)

        viewModel.getSession().observe(this){
            firebaseViewModel.fetchRekening(it.idKartu)
        }
        firebaseViewModel.getRekening().observe(this) { idRekening ->
            val query = database.child("data_transaksi").orderByChild("idRekening").equalTo(idRekening)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val transactions = mutableListOf<DataTransaksi>()
                    for (dataSnapshot in snapshot.children) {
                        val transaction = dataSnapshot.getValue(DataTransaksi::class.java)
                        transaction?.let {
                            val transactionDate = it.tglTran


                            val startDateObj = parseDate(timeFrom!!)
                            val endDateObj = parseDate(timeTo!!)
                            val transactionDateObj = parseDate(transactionDate!!)


                            if (isDateWithinRange(transactionDateObj, startDateObj, endDateObj)) {
                                when (jenisTran) {
                                    "Uang Masuk" -> {
                                        if (it.jenisTran == "CR") {
                                            transactions.add(it)
                                        }
                                    }
                                    "Uang Keluar" -> {
                                        if (it.jenisTran == "DB") {
                                            transactions.add(it)
                                        }
                                    }
                                    else -> transactions.add(it)
                                }
                            }
                        }
                    }
                    adapter.setData(transactions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MutasiActivity", "Failed to read value.", error.toException())
                }
            })

        }


    }

    private fun parseDate(dateString: String): Date {

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val formattedDateString = "$dateString/$currentYear"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(formattedDateString) ?: Date()
    }

    private fun isDateWithinRange(date: Date, startDate: Date, endDate: Date): Boolean {

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val formattedStartDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startDate)
        val formattedEndDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate)
        val formattedDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        val formattedStartDateWithYear = "$formattedStartDateString/$currentYear"
        val formattedEndDateWithYear = "$formattedEndDateString/$currentYear"
        val formattedDateWithYear = "$formattedDateString/$currentYear"


        val startDateWithYear = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(formattedStartDateWithYear) ?: startDate
        val endDateWithYear = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(formattedEndDateWithYear) ?: endDate
        val dateWithYear = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(formattedDateWithYear) ?: date


        return dateWithYear in startDateWithYear..endDateWithYear
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
                val randomIndex = (indicatorImages.indices).random()
                binding.indicatorSignalMutasi.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

    companion object {
        const val DATE_FROM = "data_from"
        const val DATE_TO = "data_to"
        const val DATA_MUTASI = "data_mutasi"
    }

}