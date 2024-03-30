package com.bank.bcamobiie.activity


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.newrek.OnboardNewRekActivity
import com.bank.bcamobiie.data.DataAkun
import com.bank.bcamobiie.databinding.ActivityWelcomeBinding
import com.bank.bcamobiie.databinding.AlertAccessLogBinding
import com.bank.bcamobiie.databinding.AlertFlazzBinding
import com.bank.bcamobiie.databinding.AlertGpsPermissionBinding
import com.bank.bcamobiie.databinding.AlertLogMbcaBinding
import com.bank.bcamobiie.databinding.AlertNewRekBinding
import com.bank.bcamobiie.databinding.AlertNotifGagalBinding
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : AppCompatActivity() {

    private lateinit var shine: View
    private lateinit var welcome: ImageView
    private var _binding: ActivityWelcomeBinding? = null
    private val binding: ActivityWelcomeBinding get() = _binding!!
    private var _alertFlazzBinding: AlertFlazzBinding? = null
    private val alertFlazzBinding: AlertFlazzBinding get() = _alertFlazzBinding!!

    private var _alertNewRekBinding: AlertNewRekBinding? = null
    private val alertNewRekBinding: AlertNewRekBinding get() = _alertNewRekBinding!!

    private var _alertMbcaBinding: AlertLogMbcaBinding? = null
    private val alertMbcaBinding: AlertLogMbcaBinding get() = _alertMbcaBinding!!

    private var _alertGpsPermission: AlertGpsPermissionBinding? = null
    private val alertGpsPermission: AlertGpsPermissionBinding get() = _alertGpsPermission!!

    private var _alertAccesPin: AlertAccessLogBinding? = null
    private val alertAccesPin: AlertAccessLogBinding get() = _alertAccesPin!!

    private var _alertFailedCodeAcces: AlertNotifGagalBinding? = null
    private val alertFailedCodeAcces: AlertNotifGagalBinding get() = _alertFailedCodeAcces!!

    private var mBcaButtonClicked = false

    private var dialogShoDown = false
    private val locationPermissionRequestCode = 1001

    private var failedAccessAttempts = 0
    private val maxFailedAttempts = 3

    private val viewModel: InputDataViewModel by viewModel()

    private lateinit var job: Job
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        job = Job()
        coroutineScope = CoroutineScope(Dispatchers.Main + job)

        binding.apply {
            btnAbout.setOnClickListener {
                intentAct(this@WelcomeActivity, AboutActivity::class.java)
            }

            viewModel.getSession().observe(this@WelcomeActivity) { isLogin ->
                if (!isLogin.isLogin) {
                    changePin.visibility = View.GONE
                } else {
                    changePin.visibility = View.VISIBLE
                    changePin.setOnClickListener {
                        startActivity(Intent(this@WelcomeActivity, NewpinActivity::class.java))
                    }
                }
            }

            mBca.setOnClickListener {
                viewModel.getSession().observe(this@WelcomeActivity) { data ->
                    if (data.isLogin) {
                        showAlertInputAccesCode()
                    } else {
                        checkGranted()
                    }
                }

            }

            klikBca.setOnClickListener {
                val url = "https://m.klikbca.com/login.jsp"
                intentUri(Intent.ACTION_VIEW, Uri.parse(url))
            }
            infoBca.setOnClickListener {
                val url =
                    "https://www.bca.co.id/promo?i=ee0f0ff25c938a910373be666d1d2cdeacbc1e23a81b67fbd43f812d8da8dcb2"
                intentUri(Intent.ACTION_VIEW, Uri.parse(url))
            }

            newRek.setOnClickListener {
                showNewRekDialog()
            }

            flazz.setOnClickListener {
                checkNfcStatus()
            }


        }
        welcome = binding.welcome
        shine = binding.shine
        coroutineScope.launch {
            delay(2) // Delay for 2ms as in the original code
            startShineEffect()
        }
    }


    private suspend fun startShineEffect() {
        while (true) {
            shineEffect()
            delay(5000L)
        }
    }


    private fun shineEffect() {
        val animation =
            TranslateAnimation(0f, welcome.width.toFloat() + shine.width.toFloat(), 0f, 0f)
        animation.duration = 550
        animation.fillAfter = false
        animation.interpolator = AccelerateDecelerateInterpolator()
        shine.startAnimation(animation)
    }

    private fun showAlertInputAccesCode() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertAccesPin = AlertAccessLogBinding.inflate(layoutInflater)
        val view = alertAccesPin.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.window?.setLayout(900, 600)
        alertAccesPin.apply {
            btnCancelAcces.setOnClickListener {
                dialog.dismiss()
            }
            btnLoginAcces.setOnClickListener {
                val inputPin = inputPinAcces.text.toString()
                viewModel.getSession().observe(this@WelcomeActivity) { dataUser ->
                    val data = dataUser.idKartu
                    val databaseRef =
                        FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference
                    lifecycleScope.launch {
                        databaseRef.child("data_akun").orderByChild("idKartu").equalTo(data)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (dataSnapshotChild in dataSnapshot.children) {
                                            val dataAkun =
                                                dataSnapshotChild.getValue(DataAkun::class.java)
                                            val kodeAcces = dataAkun?.kodeAcces
                                            val status = dataAkun?.status
                                            if (inputPin == kodeAcces && status == "AKTIF") {
                                                dialog.dismiss()
                                                intentActFinish(
                                                    this@WelcomeActivity,
                                                    MainActivity::class.java
                                                )
                                            } else if (status != "AKTIF") {
                                                showAlertFailedAccountNonActive()
                                            } else {
                                                failedAccessAttempts++

                                                if (failedAccessAttempts < 2) {
                                                    showAlertFailedCodeAcces()
                                                } else if (failedAccessAttempts == 2) {
                                                    showAlertFailedCodeAccesLast()
                                                } else if (failedAccessAttempts == maxFailedAttempts) {
                                                    showAlertFailedCodeAcces()
                                                }
                                            }
                                            break
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle cancelled event
                                }
                            })
                    }
                }
            }
        }
    }


    private fun showAlertFailedCodeAcces() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces = AlertNotifGagalBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.text = getString(R.string.kode_akses_salah_satu)

            bntnBackalertFail.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

    private fun showAlertFailedCodeAccesLast() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces = AlertNotifGagalBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.text = getString(R.string.kode_akses_salah_two)

            bntnBackalertFail.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

    private fun showAlertFailedAccountNonActive() {
        val alertBuilder = AlertDialog.Builder(this)
        _alertFailedCodeAcces = AlertNotifGagalBinding.inflate(layoutInflater)
        val view = alertFailedCodeAcces.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        dialog.window?.setLayout(850, 1000)
        alertFailedCodeAcces.apply {
            tvPesanGagal.text = getString(R.string.message_block)

            bntnBackalertFail.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

    private fun showAlertMbcaDialog() {
        if (dialogShoDown) {
            val alertBuilder = MaterialAlertDialogBuilder(this, R.style.RoundedMaterialDialog)
            _alertMbcaBinding = AlertLogMbcaBinding.inflate(layoutInflater)
            val view = alertMbcaBinding.root
            alertBuilder.setView(view)
            val dialog = alertBuilder.create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            alertMbcaBinding.apply {
                btnOkMbca.setOnClickListener {
                    openLocationSettings()
                }
            }
        }

    }

    private fun showNewRekDialog() {
        val alertBuilder = MaterialAlertDialogBuilder(this, R.style.RoundedMaterialDialog)
        _alertNewRekBinding = AlertNewRekBinding.inflate(layoutInflater)
        val view = alertNewRekBinding.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        alertNewRekBinding.apply {
            btnCobaMyBcaMob.setOnClickListener {
                val url = "https://play.google.com/store/apps/details?id=com.bca.mybca.omni.android"
                intentUri(Intent.ACTION_VIEW, Uri.parse(url))
            }
            btnTetapBcaMob.setOnClickListener {
                intentAct(this@WelcomeActivity, OnboardNewRekActivity::class.java)
                dialog.dismiss()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun checkLocationAndOpenSettingsIfDisabled() {
        if (!isLocationEnabled()) {
            dialogShoDown = true
            showAlertMbcaDialog()
        } else {
            intentAct(this, KetentuanActivity::class.java)
        }
    }

    private fun showNeedGpGrantedDialog() {
        val alertBuilder = MaterialAlertDialogBuilder(this, R.style.RoundedMaterialDialog)
        _alertGpsPermission = AlertGpsPermissionBinding.inflate(layoutInflater)
        val view = alertGpsPermission.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        alertGpsPermission.apply {
            btnTolak.setOnClickListener {
                dialog.dismiss()
            }
            btnIzinkan.setOnClickListener {
                dialog.dismiss()
                ActivityCompat.requestPermissions(
                    this@WelcomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionRequestCode
                )
            }
        }
    }

    private fun checkGranted() {
        if (ContextCompat.checkSelfPermission(
                this@WelcomeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            showNeedGpGrantedDialog()
        } else {
            mBcaButtonClicked = true
            checkLocationAndOpenSettingsIfDisabled()
        }
    }

    private fun checkNfcStatus() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            showNfcDisabledDialog()
        }
    }

    private fun showNfcDisabledDialog() {
        val alertBuilder = MaterialAlertDialogBuilder(this, R.style.RoundedMaterialDialog)
        _alertFlazzBinding = AlertFlazzBinding.inflate(layoutInflater)
        val view = alertFlazzBinding.root
        alertBuilder.setView(view)
        val dialog = alertBuilder.create()
        dialog.show()
        alertFlazzBinding.apply {
            cancelNfc.setOnClickListener {
                dialog.dismiss()
            }
            settingsNfc.setOnClickListener {
                val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun intentAct(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }

    private fun intentActFinish(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
        finish()
    }


    private fun intentUri(destination: String, uri: Uri) {
        val intent = Intent(destination, uri)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    override fun onResume() {
        super.onResume()
        if (mBcaButtonClicked) {
            if (!isLocationEnabled()) {
                mBcaButtonClicked = true
            } else {
                dialogShoDown = false
                val intent = Intent(this, KetentuanActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                mBcaButtonClicked = false
            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mBcaButtonClicked = true
                checkLocationAndOpenSettingsIfDisabled()
            }
        }

    }
}