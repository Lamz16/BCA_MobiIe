package com.bank.bcamobiie.datadummy

data class MenuInHome(
    val id : Int,
    val title: String = "",
    val menuName: String = ""
)

object DataMenuInHome {
    val listMenuMinfo = listOf(
        MenuInHome(1,menuName = "Info Saldo"),
        MenuInHome(2,menuName = "Mutasi Rekening"),
        MenuInHome(3,menuName = "Rekening Deposito"),
        MenuInHome(4,menuName = "Info Reward BCA"),
        MenuInHome(5,"Info Reksadan", "NAB Reksadana"),
        MenuInHome(6,menuName = "Saldo Reksadana"),
        MenuInHome(7, menuName = "Info Kurs"),
        MenuInHome(8,"Info RDN", "Info Saldo"),
        MenuInHome(9,menuName = "Mutasi Rekening"),
        MenuInHome(10,"Info KPR", menuName = "Inquiry Pinjaman"),
        MenuInHome(11,"Info Kartu Kredit", menuName = "Saldo"),
        MenuInHome(12,menuName = "Transaksi dan Tagihan"),
        MenuInHome(13,menuName = "Lainnya"),
        MenuInHome(14,menuName = "Inbox")
    )

    val listMenuTf = listOf(
        MenuInHome(1, title = "Daftar Transfer", "Antar Rekening"),
        MenuInHome(2, menuName ="Antar Bank"),
        MenuInHome(3, title = "Transfer", "Antar Rekening"),
        MenuInHome(4, menuName = "Antar Bank"),
        MenuInHome(5, menuName ="BCA Virtual Account"),
        MenuInHome(6, menuName ="Sakuku"),
        MenuInHome(7, menuName ="Status Transaksi  Sakuku"),
        MenuInHome(8,  menuName ="Inbox (1)"),
    )

    val listMenuPayment = listOf(
        MenuInHome(1, menuName = "Kartu Kredit / Paylater BCA"),
        MenuInHome(2, menuName = "Handphone"),
        MenuInHome(3, menuName = "Telephone"),
        MenuInHome(4, menuName = "Publik ? Utilitas"),
        MenuInHome(5, menuName = "BPJS"),
        MenuInHome(6, menuName = "Pajak"),
        MenuInHome(7, menuName = "Asuransi"),
        MenuInHome(8, menuName = "Internet"),
        MenuInHome(9, menuName = "Pinjaman"),
        MenuInHome(10, menuName = "Lainnya"),
        MenuInHome(11, menuName = "Inbox")
    )


    val listMenuEcommerce = listOf(
        MenuInHome(1, menuName = "Voucher Isi Ulang"),
        MenuInHome(2, menuName = "PLN Prabayar"),
        MenuInHome(3, menuName = "PLN Manual Advice"),
        MenuInHome(4, menuName = "Lainnya"),
        MenuInHome(5, menuName = "Inbox"),
    )


    val listMenuMadmin = listOf(
        MenuInHome(1, menuName = "Ganti PIN"),
        MenuInHome(2, menuName = "Atur OneKlik"),
        MenuInHome(3, menuName = "Atur Kuasa Debet"),
        MenuInHome(4, menuName = "Blokir Kartu Kredit"),
        MenuInHome(5, menuName = "Kontrol Kartu Kredit"),
        MenuInHome(6, menuName = "Request Limit Kartu Kredit"),
        MenuInHome(7,"Koneksi Kartu Kredit", menuName = "Daftar"),
        MenuInHome(8, menuName = "Hapus"),
        MenuInHome(9, "e-Statement Kartu Kredit",menuName = "Hapus"),
        MenuInHome(10, menuName = "Daftar"),
        MenuInHome(11, menuName = "Request Ulang"),
        MenuInHome(12, menuName = "Reset Password"),
        MenuInHome(13,"Aktivasi Kartu Kredit", menuName = "Aktivasi Kartu"),
        MenuInHome(14, menuName = "Buat/Ubah PIN"),
        MenuInHome(15,"Hapus Daftar", menuName = "Transfer Antar Rekening"),
        MenuInHome(16, menuName = "Transfer Antar Rekening"),
        MenuInHome(17, menuName = "Transfer Antar Bank"),
        MenuInHome(18, menuName = "Transfer BCA Virtual Account"),
        MenuInHome(19, menuName = "Transfer Sakuku"),
        MenuInHome(20, menuName = "Pembayaran"),
        MenuInHome(21, menuName = "Pembelian")

    )

}

