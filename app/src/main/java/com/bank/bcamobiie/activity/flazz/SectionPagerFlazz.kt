package com.bank.bcamobiie.activity.flazz

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerFlazz(activity : AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = FlazzinfoFragment()
            1 -> fragment = FlazzinboxFragment()
        }
        return  fragment as Fragment
    }
}