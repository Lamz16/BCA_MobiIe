package com.bank.bcamobiie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.menucardless.SectionPaggerAdapter
import com.bank.bcamobiie.databinding.ActivityCardlessBinding
import com.bank.bcamobiie.utils.Utils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CardlessActivity : AppCompatActivity() {

    private var _binding : ActivityCardlessBinding? = null
    private val binding : ActivityCardlessBinding get() = _binding!!

    private val indicatorImages = Utils.indicatorImages

    private val indicatorChangeDelay = Utils.indicatorChangeDelay

    private var indicatorChangeJob = Utils.indicatorChangeJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCardlessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startIndicatorChangeJob()

        val sectionPaggerAdapter = SectionPaggerAdapter(this)
        val viewPager : ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPaggerAdapter
        val tabsPage = binding.tabLayout
        TabLayoutMediator(tabsPage, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

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
                binding.indicatorSignalcardless.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_transaksi,
            R.string.tab_inbox
        )
    }

}