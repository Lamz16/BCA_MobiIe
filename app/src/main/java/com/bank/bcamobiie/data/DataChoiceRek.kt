package com.bank.bcamobiie.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataChoiceRek(
    val nama : String? = "",
    val noRek : String? = ""
) : Parcelable
