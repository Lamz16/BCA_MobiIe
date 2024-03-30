package com.bank.bcamobiie.activity.menucardless

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPaggerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = CardlessMenuFragment()
            1 -> fragment = InboxFragment()
        }
        return  fragment as Fragment
    }
}