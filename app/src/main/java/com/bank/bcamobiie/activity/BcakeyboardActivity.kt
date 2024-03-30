package com.bank.bcamobiie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.bcakeyboard.SectionPagerAdapterBcakey
import com.bank.bcamobiie.activity.menucardless.SectionPaggerAdapter
import com.bank.bcamobiie.databinding.ActivityBcakeyboardBinding
import com.google.android.material.tabs.TabLayoutMediator

class BcakeyboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBcakeyboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBcakeyboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sectionPaggerAdapter = SectionPagerAdapterBcakey(this)
        val viewPager : ViewPager2 = binding.viewPagerBcakey

        viewPager.adapter = sectionPaggerAdapter
        val tabsPage = binding.tabLayoutBcakey
        TabLayoutMediator(tabsPage, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.btnMulaiKeyboard.visibility = View.INVISIBLE
                    binding.tvMulaiKeyboard.visibility = View.INVISIBLE
                    binding.tabLayoutBcakey.visibility = View.VISIBLE
                } else if (position == 1) {
                    binding.btnMulaiKeyboard.visibility = View.VISIBLE
                    binding.tvMulaiKeyboard.visibility = View.VISIBLE
                    binding.tabLayoutBcakey.visibility = View.INVISIBLE
                }
            }
        })

    }



    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_keyboard_one,
            R.string.tab_keboard_two
        )
    }


}