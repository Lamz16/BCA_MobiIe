package com.bank.bcamobiie.datadummy

import com.bank.bcamobiie.R

data class MenuConfiguration(
    val id : Int,
    val image : Int,
    val desc : String
)


val dataMenu = listOf<MenuConfiguration>(
    MenuConfiguration(1, R.drawable.clickmenu1, "m-Info"),
    MenuConfiguration(2, R.drawable.clickmenu2, "m-Transfer"),
    MenuConfiguration(3, R.drawable.clickmenu3, "m-Payment"),
    MenuConfiguration(4, R.drawable.clickmenu4, "m-Commerce"),
    MenuConfiguration(5, R.drawable.clickmenu5, "Cardless"),
    MenuConfiguration(6, R.drawable.clickmenu6, "m-Admin"),
    MenuConfiguration(7, R.drawable.clickmenu7, "BCA \n Keyboard"),
    MenuConfiguration(8, R.drawable.clickmenu8, "Flazz"),
    MenuConfiguration(9, R.drawable.clickmenu9, "BagiBagi"),
    MenuConfiguration(10, R.drawable.clickmenu10, "Lifestyle")

)


data class MenuConfMyAcc(
    val id: Int,
    val image : Int,
    val desc: String
)

val dataMenuMyAcc = listOf(
    MenuConfMyAcc(1,R.drawable.clickmenuacc1, "kontrol"),
    MenuConfMyAcc(2,R.drawable.clickmenuacc2, "Atur Limit"),
    MenuConfMyAcc(3,R.drawable.clickmenuacc3, "Blokir"),
    MenuConfMyAcc(4,R.drawable.clickmenuacc4, "Ganti Kartu"),
)

val dataMenuCardless = listOf(
    MenuConfMyAcc(1,R.drawable.clickcardless1, "Tarik Tunai"),
    MenuConfMyAcc(2,R.drawable.clickcardless2, "Setor Tunai"),
    MenuConfMyAcc(3,R.drawable.clickcardless3, "Cabang"),
)
