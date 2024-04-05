package com.bank.bcamobiie.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTransaksi(
    val idTransaksi: String? = "",
    val idRekening : String? = "",
    val jenisTran : String? = "",
    val jumlah : String = "",
    val tglTran : String? ="",
    val keterangan : String? = "",
    val layanan : String? = ""
) : Parcelable {

    fun contains(query: String): Boolean {
        return idTransaksi?.contains(query, ignoreCase = true) == true ||
                idRekening?.contains(query, ignoreCase = true) == true ||
                jenisTran?.contains(query, ignoreCase = true) == true ||
                jumlah.contains(query, ignoreCase = true) ||
                tglTran?.contains(query, ignoreCase = true) == true ||
                keterangan?.contains(query, ignoreCase = true) == true ||
                layanan?.contains(query, ignoreCase = true) == true
    }
}
