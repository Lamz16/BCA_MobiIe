package com.bank.bcamobiie.datastore

data class Userdata(
    val idKartu : String,
    val pin : String,
    val pw : String,
    val isLogin : Boolean = false
)