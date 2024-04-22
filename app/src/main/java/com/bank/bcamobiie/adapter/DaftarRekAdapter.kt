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

        private var currentItem: DataDaftarRek? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    it.isSelected = !it.isSelected  // Toggle isSelected state
                    updateSelectedIcon(it.isSelected)
                }
            }
        }

        fun bind(daftarRek: DataDaftarRek) {
            currentItem = daftarRek  // Assign current item

            atasRek.text = daftarRek.noRek
            atasNama.text = daftarRek.nama
            updateSelectedIcon(daftarRek.isSelected)
        }

        private fun updateSelectedIcon(isSelected: Boolean) {
            if (isSelected) {
                this.isSelected.setImageResource(R.drawable.ic_selected)  // Ganti dengan ikon yang sesuai
            } else {
                this.isSelected.setImageResource(R.drawable.ic_select)  // Ganti dengan ikon yang sesuai
            }
        }
    }
}

