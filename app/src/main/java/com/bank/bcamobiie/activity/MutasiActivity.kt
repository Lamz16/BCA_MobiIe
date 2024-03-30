package com.bank.bcamobiie.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Html
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivityMutasiBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MutasiActivity : AppCompatActivity() {

    private var _binding : ActivityMutasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var editTextDateDari: EditText
    private lateinit var editTextDateSampai: EditText
    private val calendarDari = Calendar.getInstance()
    private val calendarSampai = Calendar.getInstance()

    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMutasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        editTextDateDari = binding.dateDari
        editTextDateSampai = binding.dateSampai

        startIndicatorChangeJob()

        setInitialDates()

        editTextDateDari.setOnClickListener {
            showDatePickerDialogDari()
        }

        editTextDateSampai.setOnClickListener {
            showDatePickerDialogSampai()
        }

        val textView = findViewById<TextView>(R.id.tvCatatan)
        val listText = getString(R.string.catatan)
        textView.text = Html.fromHtml(listText, Html.FROM_HTML_MODE_COMPACT)

    }

    override fun onDestroy() {
        super.onDestroy()
        indicatorChangeJob?.cancel()
        _binding = null
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignalMutasi.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }


    private fun setInitialDates() {
        val dateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendarDari.time)
        editTextDateDari.setText(formattedDate)
        editTextDateSampai.setText(formattedDate)
    }

    private fun showDatePickerDialogDari() {
        val datePickerDialog = DatePickerDialog(
            this, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendarDari.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(calendarDari.time)
                editTextDateDari.setText(formattedDate)
            },
            calendarDari.get(Calendar.YEAR),
            calendarDari.get(Calendar.MONTH),
            calendarDari.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showDatePickerDialogSampai() {
        val datePickerDialog = DatePickerDialog(
            this, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendarSampai.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(calendarSampai.time)
                editTextDateSampai.setText(formattedDate)
            },
            calendarSampai.get(Calendar.YEAR),
            calendarSampai.get(Calendar.MONTH),
            calendarSampai.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
