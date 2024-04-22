package com.bank.bcamobiie.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivityMutasiBinding
import com.bank.bcamobiie.databinding.AlertPeriodeSevenBinding
import com.bank.bcamobiie.databinding.AlertWrongCodeAccesBinding
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MutasiActivity : AppCompatActivity() {

    private var _binding : ActivityMutasiBinding? = null
    private val binding get() = _binding!!
    private var _alertWrong: AlertPeriodeSevenBinding? = null
    private val alertWrong: AlertPeriodeSevenBinding get() = _alertWrong!!
    private lateinit var editTextDateDari: EditText
    private lateinit var editTextDateSampai: EditText
    private val calendarDari = Calendar.getInstance()
    private val calendarSampai = Calendar.getInstance()
    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob
    private var jenisTran : String = ""
    private val viewModel: InputDataViewModel by viewModel()
    private val firebaseViewModel: FirebaseDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMutasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        editTextDateDari = binding.dateDari
        editTextDateSampai = binding.dateSampai

        startIndicatorChangeJob()

        jenisTran= "Semua"

        binding.opsiMutasi.setOnClickListener {
            val options = arrayOf("Semua", "Uang Masuk", "Uang Keluar")

            val adapter = object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                options
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    textView.text = options[position]
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

                    val paddingInDp = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        16f,
                        resources.displayMetrics
                    ).toInt()
                    textView.setPadding(paddingInDp, 8, paddingInDp, 8)

                    val textColor = ContextCompat.getColorStateList(this@MutasiActivity, R.color.text_alert_selector)
                    textView.setTextColor(textColor)
                    textView.background = ContextCompat.getDrawable(this@MutasiActivity, R.drawable.list_item_selector)

                    return view
                }
            }

            val builder = AlertDialog.Builder(this)

            builder.setAdapter(adapter) { dialog, which ->
                val selectedOption = options[which]
                binding.opsiMutasi.text = selectedOption
                jenisTran = selectedOption
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.list_item_outline)

            dialog.show()

            // Set divider
            dialog.listView.divider = ColorDrawable(ContextCompat.getColor(this, R.color.line_alert))
            dialog.listView.dividerHeight = resources.getDimensionPixelSize(R.dimen.divider_height)

            val paddingInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                48f,
                resources.displayMetrics
            ).toInt()
            dialog.listView.setPadding(paddingInDp, 0, paddingInDp, 0)
        }

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

        binding.btnSend.setOnClickListener {
            val diffInMillis = calendarSampai.timeInMillis - calendarDari.timeInMillis

            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)


            if (diffInDays > 7) {
               showAlert()
            } else {

                // Format tanggal dari dan tanggal sampai
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fromDate = dateFormat.format(calendarDari.time)
                val toDate = dateFormat.format(calendarSampai.time)

                // Membuat intent dan mengirimkan data tanggal
                val intent = Intent(this, MutasilistActivity::class.java)
                intent.putExtra(MutasilistActivity.DATA_MUTASI, jenisTran)
                intent.putExtra(MutasilistActivity.DATE_FROM, fromDate)
                intent.putExtra(MutasilistActivity.DATE_TO, toDate)
                startActivity(intent)
            }
        }

        viewModel.getSession().observe(this){
            firebaseViewModel.fetchRekening(it.idKartu)
        }
        firebaseViewModel.getRekening().observe(this) { rekening ->
            binding.tvNorek.text = rekening
        }


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
                val randomIndex = (indicatorImages.indices).random()
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

    private fun showAlert()  {
        val alertBuilder = AlertDialog.Builder(this)
        _alertWrong = AlertPeriodeSevenBinding.inflate(layoutInflater)
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

}
