package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivityNewpinBinding
import com.bank.bcamobiie.databinding.AlertFailedPasswordBinding
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

class NewpinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewpinBinding
    private var _alertFailedCodeAcces: AlertFailedPasswordBinding? = null
    private val alertFailedCodeAcces: AlertFailedPasswordBinding get() = _alertFailedCodeAcces!!
    private lateinit var database: DatabaseReference
    private lateinit var kodeAksesBaru: String

    private val viewModel: InputDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewpinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference

        binding.apply {
            btnCancelNewpin.setOnClickListener {
                finish()
            }

                btnOkNewpin.setOnClickListener {
                    viewModel.getSession().observe(this@NewpinActivity) { dataUser ->
                    val oldCode = inputNew1.text.toString()
                    kodeAksesBaru = inputNew2.text.toString()

                    updateKodeAkses(dataUser.pin, oldCode, dataUser.idKartu)
                }

            }

        }

    }

    private fun updateKodeAkses(mainCode : String , oldCode : String, idKartu : String) {

                if (oldCode ==  mainCode){
                    Log.d("Newpin", "mainCode: ${mainCode}")
                    Log.d("Newpin", "oldCode: ${mainCode}")
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
                                                    val intent = Intent(this@NewpinActivity, WelcomeActivity::class.java)
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
                }else {
                    showAlertFailedCardId()
                }

    }


    private fun showAlertFailedCardId() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces= AlertFailedPasswordBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.setTextColor(ContextCompat.getColor(this@NewpinActivity, R.color.blue))
            tvPesanGagal.text = getString(R.string.invalid_password)
            bntnBackalertFail.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

}