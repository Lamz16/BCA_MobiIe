package com.bank.bcamobiie.activity.flazz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.databinding.FragmentFlazzinfoBinding
import com.bank.bcamobiie.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class FlazzinfoFragment : Fragment() {

    private lateinit var binding : FragmentFlazzinfoBinding


    private val indicatorImages = Utils.indicatorFlazz

    private val indicatorChangeDelay = Utils.flazzChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlazzinfoBinding.inflate(inflater, container, false)
        startAnimationChangeJob()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        indicatorChangeJob?.cancel()
    }

    private fun startAnimationChangeJob() {
        indicatorChangeJob?.cancel()

        var currentIndex = 0 // Indeks gambar saat ini

        indicatorChangeJob = lifecycleScope.launch {
            while (isActive) {
                // Tampilkan gambar sesuai dengan indeks saat ini
                binding.imgFlazz.setImageResource(indicatorImages[currentIndex])

                // Pindah ke gambar berikutnya
                currentIndex = (currentIndex + 1) % indicatorImages.size

                delay(indicatorChangeDelay)
            }
        }
    }

}