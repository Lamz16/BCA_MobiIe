package com.bank.bcamobiie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.data.DataChoiceRek
import java.util.Locale

class ChoiceRekAdapter(private var daftarRekList: List<DataChoiceRek>,  private val listener: OnItemClickListener) : RecyclerView.Adapter<ChoiceRekAdapter.ChoiceViewHolder>() {

    var daftarRekListFull: List<DataChoiceRek> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(dataChoiceRek: DataChoiceRek)
    }

    inner class ChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val atasRek: TextView = itemView.findViewById(R.id.atasRek)
        private val atasNama: TextView = itemView.findViewById(R.id.atasNama)
        private var currentItem: DataChoiceRek? = null

        fun bind(daftarRek: DataChoiceRek) {
            currentItem = daftarRek

            atasRek.text = daftarRek.noRek
            atasNama.text = daftarRek.nama
            itemView.setOnClickListener {
                listener.onItemClick(currentItem!!)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_choicerek_row, parent, false)

        return ChoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChoiceViewHolder, position: Int) {
        val daftarRek = daftarRekList[position]
        holder.bind(daftarRek)
    }

    override fun getItemCount() = daftarRekList.size

    fun filterList(filterText: String) {
        daftarRekList = if (filterText.isEmpty()) {
            daftarRekListFull
        } else {
            daftarRekListFull.filter {
                it.nama?.lowercase(Locale.getDefault())?.contains(filterText.lowercase(Locale.getDefault())) == true ||
                        it.noRek?.contains(filterText) == true
            }
        }
        notifyDataSetChanged()
    }
}