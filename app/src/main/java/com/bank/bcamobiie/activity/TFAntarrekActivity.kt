package com.bank.bcamobiie.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.bank.bcamobiie.databinding.AlertNewRekBinding
import com.bank.bcamobiie.databinding.InputBeritaDialogBinding
import com.bank.bcamobiie.databinding.InputUangDialogBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class TFAntarrekActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTfantarrekBinding

    private var _alertInputUang: InputUangDialogBinding? = null
    private val alertInputUang: InputUangDialogBinding get() = _alertInputUang!!

    private var _alertInputBerita: InputBeritaDialogBinding? = null
    private val alertInputBerita: InputBeritaDialogBinding get() = _alertInputBerita!!

    private lateinit var edtNominalTf : EditText
    private val decimalFormat = DecimalFormat("#,###")
    private var isFormatting = false

    private val indicatorImages = Utils.indicatorImages
    private val indicatorChangeDelay = Utils.indicatorChangeDelay
    private var indicatorChangeJob = Utils.indicatorChangeJob
    private lateinit var choiceRek: DataChoiceRek
    private lateinit var textRek: String
    private lateinit var jenisCurrency: String
    private lateinit var nominalTf : String
    private lateinit var beritaTf : String

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

        if (dataUser != null) {
            choiceRek = dataUser
            textRek = "${choiceRek.noRek} - ${choiceRek.nama}"
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

            // Set divider
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
        val dpiCategory = resources.configuration.densityDpi
        val widthMultiplier = when (dpiCategory) {
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
            nominalTf = nominal
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
        val dpiCategory = resources.configuration.densityDpi
        val widthMultiplier = when (dpiCategory) {
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

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }


}