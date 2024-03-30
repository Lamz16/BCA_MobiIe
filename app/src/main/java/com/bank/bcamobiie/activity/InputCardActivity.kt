package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.SuccesInputActivity
import com.bank.bcamobiie.databinding.ActivityInputCardBinding
import com.bank.bcamobiie.databinding.AlertNotifGagalBinding
import com.bank.bcamobiie.utils.Utils.removeHyphens
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputCardBinding
    private lateinit var databaseRef: DatabaseReference

    private var _alertFailedCodeAcces: AlertNotifGagalBinding? = null
    private val alertFailedCodeAcces: AlertNotifGagalBinding get() = _alertFailedCodeAcces!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        databaseRef = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference

        processInput()
    }

    private fun processInput() {
        val editText = binding.inputNumber

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().replace("-", "")
                    .replace(" ", "")
                val formattedText = StringBuilder()

                for (i in text.indices) {
                    formattedText.append(text[i])
                    if ((i + 1) % 4 == 0 && i != text.lastIndex) {
                        formattedText.append("-")
                    }
                }

                if (formattedText.toString() != editable.toString()) {
                    editText.removeTextChangedListener(this)
                    editText.setText(formattedText.toString())
                    editText.setSelection(formattedText.length)
                    editText.addTextChangedListener(this)
                }
            }
        })

        binding.btnOkInput.setOnClickListener {
            val cardNumber = editText.text.toString().removeHyphens()
            if (isValidCardNumber(cardNumber)) {
                checkCardNumberInFirebase(cardNumber)
            } else {
                showAlertFailedlengthCard()
            }
        }
    }

    private fun checkCardNumberInFirebase(cardNumber: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            databaseRef.child("data_rekening").orderByChild("idKartu").equalTo(cardNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val intent = Intent(this@InputCardActivity, SuccesInputActivity::class.java)
                            intent.putExtra(SuccesInputActivity.DATA_NOREK, cardNumber)
                            startActivity(intent)
                        } else {
                            showAlertFailedCardId()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        return cardNumber.length == 16
    }

    private fun showAlertFailedCardId() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces= AlertNotifGagalBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.setTextColor(ContextCompat.getColor(this@InputCardActivity, R.color.blue))
            tvPesanGagal.text = getString(R.string.invalid_nomor_kartu)
            bntnBackalertFail.setOnClickListener {
                binding.inputNumber.text.clear()
                dialog.dismiss()
            }
        }

    }

    private fun showAlertFailedlengthCard() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces= AlertNotifGagalBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.setTextColor(ContextCompat.getColor(this@InputCardActivity, R.color.blue))
            tvPesanGagal.text = getString(R.string.invalid_jumlah_nomor_kartu)
            bntnBackalertFail.setOnClickListener {
                binding.inputNumber.text.clear()
                dialog.dismiss()
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
