package com.bank.bcamobiie.utils

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import com.bank.bcamobiie.R
import kotlinx.coroutines.Job
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun getCurrentDateWithFormat(): String {
        val dateFormat = SimpleDateFormat("ddMMyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        return dateFormat.format(currentDate)
    }

    fun getCurrentTimeWithFormat(): String {
        val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        return timeFormat.format(currentTime)
    }

    fun getCurrentDateTime(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun sendMessage(phoneNumber: String, message: String) {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", message)
    }

    fun formatToRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getNumberInstance(localeID) as DecimalFormat

        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2

        val symbols = numberFormat.decimalFormatSymbols
        symbols.groupingSeparator = ','
        symbols.decimalSeparator = '.'
        numberFormat.decimalFormatSymbols = symbols

        val formattedAmount = numberFormat.format(amount.toDouble() / 100)

        return "Rp. $formattedAmount"
    }

    val sendMessageTrigger = sendMessage("89888", "BCA mobile 1\n" +
            "Kirim SMS utk AKTIVASI\n" +
            "HATI HATI Modus Penipuan\n" +
            "No. Ref:VGRCAOEK1263668F\n" +
            "Tgl/Jam: ${Utils.getCurrentDateWithFormat()} ${Utils.getCurrentTimeWithFormat()}")


    val indicatorImages = listOf(
        R.drawable.navconngreen,
        R.drawable.navconnblue,
        R.drawable.navconnred,
    )

    val indicatorChangeDelay = 2500L

    var indicatorChangeJob: Job? = null

    fun String.addSpaceEveryFourCharacters(): String {
        val regex = "(.{4})(?=.)".toRegex()
        return this.replace(regex, "$1 ")
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == inputRequestCode){
//            if (isInputActive){
//                isInputActive = false
//                val intent = Intent(this, SuccesInputActivity::class.java)
//                startActivity(intent)
//            }
//
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (isInputActive){
//            isInputActive = false
//            val intent = Intent(this, SuccesInputActivity::class.java)
//            startActivity(intent)
//        }
//    }

    fun String.removeHyphens(): String {
        return this.replace("-", "")
    }

}