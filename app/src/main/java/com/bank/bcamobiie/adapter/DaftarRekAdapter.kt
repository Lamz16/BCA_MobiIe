package com.bank.bcamobiie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.data.DataDaftarRek

class DaftarRekAdapter(private val daftarRekList: List<DataDaftarRek>) :
    RecyclerView.Adapter<DaftarRekAdapter.DaftarRekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaftarRekViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_daftar_rek, parent, false)  // Menggunakan layout item row Anda
        return DaftarRekViewHolder(view)
    }

    override fun onBindViewHolder(holder: DaftarRekViewHolder, position: Int) {
        val daftarRek = daftarRekList[position]
        holder.bind(daftarRek)
    }

    override fun getItemCount(): Int {
        return daftarRekList.size
    }

    inner class DaftarRekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val atasRek: TextView = itemView.findViewById(R.id.atasRek)
        private val atasNama: TextView = itemView.findViewById(R.id.atasNama)
        private val isSelected: ImageView = itemView.findViewById(R.id.isSelected)

        fun bind(daftarRek: DataDaftarRek) {
            atasRek.text = daftarRek.noRek
            atasNama.text = daftarRek.nama
        }
    }
}

