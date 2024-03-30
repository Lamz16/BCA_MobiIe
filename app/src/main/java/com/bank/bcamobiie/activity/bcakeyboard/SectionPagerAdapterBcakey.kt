package com.bank.bcamobiie.activity.bcakeyboard

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapterBcakey(activity : AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = Bcapageone()
            1 -> fragment = Bcapagetwo()
        }
        return  fragment as Fragment
    }
}