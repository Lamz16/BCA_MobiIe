package com.bank.bcamobiie.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivityQrisBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QrisActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQrisBinding
    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        startIndicatorChangeJob()

        binding.btnRefreshQr.setOnClickListener {
            binding.btnRefreshQr.visibility = View.INVISIBLE
            binding.messageQr.visibility = View.INVISIBLE

            lifecycleScope.launch {
                delay(500)
                val progressBar = binding.progressBar
                setProgressBarColor(progressBar, R.color.blue)
                progressBar.visibility = View.VISIBLE

                delay(2000)

                progressBar.visibility = View.INVISIBLE

                binding.btnRefreshQr.visibility = View.VISIBLE
                binding.messageQr.visibility = View.VISIBLE
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
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignalQr.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

    fun setProgressBarColor(progressBar: ProgressBar, color: Int) {
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(progressBar.context, color), PorterDuff.Mode.SRC_IN)
    }

}
