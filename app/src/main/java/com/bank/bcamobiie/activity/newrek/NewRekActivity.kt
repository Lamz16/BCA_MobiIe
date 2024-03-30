package com.bank.bcamobiie.activity.newrek

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bank.bcamobiie.R

class NewRekActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_rek)
        supportActionBar?.hide()

        val fragmentManager = supportFragmentManager
        val nabungKiniFragment = NabungKiniFragment()
        val fragment = fragmentManager.findFragmentByTag(NabungKiniFragment::class.java.simpleName)
        if (fragment !is NabungKiniFragment) {
            fragmentManager.commit {
                add(R.id.frame_containerNewrek, nabungKiniFragment, NabungKiniFragment::class.java.simpleName)
            }
        }

    }
}