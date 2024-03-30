package com.bank.bcamobiie.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.ActivityMainBinding
import com.bank.bcamobiie.databinding.AlertinfologoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var _alertLogout: AlertinfologoutBinding? = null
    private val alertLogout: AlertinfologoutBinding get() = _alertLogout!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val indexes = 2
            navView.menu.getItem(indexes).isEnabled = false
            navView.children.forEach { child ->
            val anim = ScaleAnimation(1f, 1f, 1f, 1f)
            anim.duration = 0
            child.startAnimation(anim) }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> navView.menu.findItem(R.id.navigation_home)?.isCheckable = true

                in listOf(R.id.navigation_minfo, R.id.navigation_mTf, R.id.navigation_mPayment, R.id.navigation_mEcomerce, R.id.navigation_mAdmin,) -> {
                    navView.menu.findItem(R.id.navigation_home)?.isCheckable = false
                }
            }

        }
        binding.btnQris.setOnClickListener {
            startActivity(Intent(this, QrisActivity::class.java))
        }

        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_transaksi -> {
                    showPopupMenu(navView)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_transaksi-> {
               showPopupMenu(binding.navView)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.inflate(R.menu.transaksi_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_mTf -> {
                    Toast.makeText(this, "Menu 1 Selected", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.navigation_mTf)
                    true
                }
                R.id.navigation_mPayment -> {
                    Toast.makeText(this, "Menu 2 Selected", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.navigation_mPayment)
                    true
                }
                R.id.navigation_mEcomerce -> {
                    Toast.makeText(this, "Menu 3 Selected", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.navigation_mEcomerce)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }



    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.navigation_home) {
            navController.navigate(R.id.navigation_home)
        } else {
            showAlertlogout()
        }
    }

    private fun showAlertlogout() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertLogout= AlertinfologoutBinding.inflate(layoutInflater)
        val view = alertLogout.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(900, 1050)
        alertLogout.apply {
            btnCancelLogout.setOnClickListener {
                dialog.dismiss()
            }
            btnLogout.setOnClickListener {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

    }

}