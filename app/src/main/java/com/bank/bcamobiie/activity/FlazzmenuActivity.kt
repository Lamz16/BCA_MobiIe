package com.bank.bcamobiie.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.flazz.SectionPagerFlazz
import com.bank.bcamobiie.databinding.ActivityFlazzmenuBinding
import com.bank.bcamobiie.utils.Utils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FlazzmenuActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityFlazzmenuBinding
    private val binding: ActivityFlazzmenuBinding
        get() {
            if (!::_binding.isInitialized) {
                _binding = ActivityFlazzmenuBinding.inflate(layoutInflater)
            }
            return _binding
        }
    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    private var isButtonPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFlazzmenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startIndicatorChangeJob()

        val sectionPagerAdapter = SectionPagerFlazz(this)
        val viewPager: ViewPager2 = binding.viewPagerFlazz

        viewPager.adapter = sectionPagerAdapter
        val tabsPage = binding.tabLayoutFlazz
        TabLayoutMediator(tabsPage, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnLgNotif.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            }
        })

        binding.btnLgNotif.setOnClickListener {
            isButtonPressed = !isButtonPressed
            updateButtonBackground()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        indicatorChangeJob?.cancel()
        _binding
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (indicatorImages.indices).random()
                binding.indicatorFlazz.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }


    private fun updateButtonBackground() {
        if (isButtonPressed) {
            binding.btnLgNotif.setBackgroundResource(R.drawable.btn_delete_up)
        } else {
            binding.btnLgNotif.setBackgroundResource(R.drawable.btn_edit_up)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_info,
            R.string.tab_inbox
        )
    }
}
