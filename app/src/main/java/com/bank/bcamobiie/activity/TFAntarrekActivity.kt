package com.bank.bcamobiie.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.data.DataChoiceRek
import com.bank.bcamobiie.databinding.ActivityTfantarrekBinding
import com.bank.bcamobiie.databinding.AlertInputPinBinding
import com.bank.bcamobiie.databinding.AlertSuccestfAntarrekBinding
import com.bank.bcamobiie.databinding.InputBeritaDialogBinding
import com.bank.bcamobiie.databinding.InputUangDialogBinding
import com.bank.bcamobiie.utils.Utils
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class TFAntarrekActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTfantarrekBinding

    private var _alertInputUang: InputUangDialogBinding? = null
    private val alertInputUang: InputUangDialogBinding get() = _alertInputUang!!

    private var _alertInputBerita: InputBeritaDialogBinding? = null
    private val alertInputBerita: InputBeritaDialogBinding get() = _alertInputBerita!!

    private var _alertTfDone: AlertSuccestfAntarrekBinding? = null
    private val alertTfDone: AlertSuccestfAntarrekBinding get() = _alertTfDone!!

    private var _alertInputPin: AlertInputPinBinding? = null
    private val alertInputPin: AlertInputPinBinding get() = _alertInputPin!!

    private lateinit var edtNominalTf : EditText
    private val decimalFormat = DecimalFormat("#,###")
    private var isFormatting = false

    private val viewModel: InputDataViewModel by viewModel()

    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob
    private lateinit var choiceRek: DataChoiceRek
    private lateinit var textRek: String
    private lateinit var atasNameRek: String
    private lateinit var noRek: String
    private lateinit var jenisCurrency: String
    private lateinit var nominalTf : String
    private lateinit var beritaTf : String
    private lateinit var pin : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfantarrekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startIndicatorChangeJob()

        binding.getNorek.setOnClickListener {
            startActivity(Intent(this, ChoicerekActivity::class.java))
        }

        val dataUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KE_REK, DataChoiceRek::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KE_REK)
        }

        viewModel.getSession().observe(this@TFAntarrekActivity){ data ->
            pin = data.pw
        }

        if (dataUser != null) {
            choiceRek = dataUser
            textRek = "${choiceRek.noRek} - ${choiceRek.nama}"
            atasNameRek = choiceRek.nama!!
            noRek = choiceRek.noRek!!
            binding.choiceRek.text = textRek
        }

        jenisCurrency = "Rp"
        binding.getCurrency.setOnClickListener {
            val options = arrayOf("Rp", "SGD", "USD")

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

                    val textColor = ContextCompat.getColorStateList(
                        this@TFAntarrekActivity,
                        R.color.text_alert_selector
                    )
                    textView.setTextColor(textColor)
                    textView.background = ContextCompat.getDrawable(
                        this@TFAntarrekActivity,
                        R.drawable.list_item_selector
                    )

                    return view
                }
            }

            val builder = AlertDialog.Builder(this)

            builder.setAdapter(adapter) { dialog, which ->
                val selectedOption = options[which]
                binding.tvCurrency.text = selectedOption
                jenisCurrency = selectedOption
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.list_item_outline)

            dialog.show()

            dialog.listView.divider =
                ColorDrawable(ContextCompat.getColor(this, R.color.line_alert))
            dialog.listView.dividerHeight = resources.getDimensionPixelSize(R.dimen.divider_height)

            val paddingInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                48f,
                resources.displayMetrics
            ).toInt()
            dialog.listView.setPadding(paddingInDp, 0, paddingInDp, 0)
        }
        binding.apply {
            setNominal.setOnClickListener {
                setNominal()
            }
            setBerita.setOnClickListener {
                setBerita()
            }
            btnSend.setOnClickListener {
                setPin()
            }
        }

    }

    private fun setNominal(){
        val alertBuilder = AlertDialog.Builder(this)
        _alertInputUang = InputUangDialogBinding.inflate(layoutInflater)
        val view = alertInputUang.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
        val widthMultiplier = when (resources.configuration.densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> 0.80 // mdpi
            DisplayMetrics.DENSITY_HIGH -> 0.75   // hdpi
            DisplayMetrics.DENSITY_XHIGH -> 0.85  // xhdpi (80%)
            DisplayMetrics.DENSITY_XXHIGH -> 0.90  // xxhdpi (90%)
            DisplayMetrics.DENSITY_XXXHIGH -> 0.90 // xxxhdpi (90%)
            else -> 0.90
        }
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * widthMultiplier).toInt()

        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)

        edtNominalTf = alertInputUang.inputNominal
        edtNominalTf.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isFormatting) {
                    isFormatting = true

                    val text = s.toString()
                    val cleanString = text.replace(".", "")

                    val formattedText = if (cleanString.isEmpty()) {
                        ""
                    } else {
                        val parsed = cleanString.toDouble()
                        decimalFormat.format(parsed)
                    }

                   edtNominalTf.setText(formattedText)
                    edtNominalTf.setSelection(formattedText.length)

                    isFormatting = false
                }
            }
        })

        alertInputUang.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        alertInputUang.okBtn.setOnClickListener {
            val nominal = edtNominalTf.text.toString()
            binding.nominalTf.text = nominal
            nominalTf = nominal.replace(".", "")
            dialog.dismiss()
        }
    }

    private fun setBerita(){
        val alertBuilder = AlertDialog.Builder(this)
        _alertInputBerita = InputBeritaDialogBinding.inflate(layoutInflater)
        val view = alertInputBerita.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
        val widthMultiplier = when (resources.configuration.densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> 0.80 // mdpi
            DisplayMetrics.DENSITY_HIGH -> 0.75   // hdpi
            DisplayMetrics.DENSITY_XHIGH -> 0.85  // xhdpi (80%)
            DisplayMetrics.DENSITY_XXHIGH -> 0.90  // xxhdpi (90%)
            DisplayMetrics.DENSITY_XXXHIGH -> 0.90 // xxxhdpi (90%)
            else -> 0.95
        }
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * widthMultiplier).toInt()

        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        alertInputBerita.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        alertInputBerita.okBtn.setOnClickListener {
            val berita = alertInputBerita.inputBerita.text.toString()
            binding.setTvBerita.text = berita
            beritaTf = berita
            dialog.dismiss()
        }
    }

    private fun setPin(){
        val alertBuilder = AlertDialog.Builder(this)
        _alertInputPin = AlertInputPinBinding.inflate(layoutInflater)
        val view = alertInputPin.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
        val widthMultiplier = when (resources.configuration.densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> 0.80
            DisplayMetrics.DENSITY_HIGH -> 0.75
            DisplayMetrics.DENSITY_XHIGH -> 0.85
            DisplayMetrics.DENSITY_XXHIGH -> 0.90
            DisplayMetrics.DENSITY_XXXHIGH -> 0.90
            else -> 0.82
        }
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * widthMultiplier).toInt()

        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)

        alertInputPin.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        alertInputPin.okBtn.setOnClickListener {
            val pinUser = alertInputPin.inputPin.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(alertInputPin.inputPin.windowToken, 0)

           if (pinUser == pin){
               dialog.dismiss()
               showAlertSuccessTf()
               Log.d("Input Pin", "setPin: $pinUser")
           }
        }
    }

    private fun showAlertSuccessTf() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertTfDone= AlertSuccestfAntarrekBinding.inflate(layoutInflater)
        val view = alertTfDone.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        alertTfDone.tvDateTf.text =Utils.getCurrentDateTime2()
        alertTfDone.destinationRek.text = resources.getString(R.string.ke, noRek)
        alertTfDone.atasNamaRek.text = atasNameRek
        alertTfDone.beritaTf.text = beritaTf
        val nominalTfFormat = nominalTf.let { Utils.formatToRupiah(it.toLong()) }
        alertTfDone.nominalTf.text = nominalTfFormat

        val widthMultiplier = when (resources.configuration.densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> 0.75 // mdpi
            DisplayMetrics.DENSITY_HIGH -> 0.78   // hdpi
            DisplayMetrics.DENSITY_XHIGH -> 0.80   // xhdpi (80%)
            DisplayMetrics.DENSITY_XXHIGH -> 0.86  // xxhdpi (90%)
            DisplayMetrics.DENSITY_XXXHIGH -> 0.90 // xxxhdpi (90%)
            else -> 0.75
        }
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * widthMultiplier).toInt()

        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        alertTfDone.apply {
            btnOkSuccesTf.setOnClickListener {
                dialog.dismiss()
            }
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

    companion object {
        const val KE_REK = "ke rekening"
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}