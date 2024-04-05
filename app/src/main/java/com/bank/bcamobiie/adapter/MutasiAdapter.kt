package com.bank.bcamobiie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.data.DataTransaksi
import com.bank.bcamobiie.utils.Utils

class MutasiAdapter(private var menuList: List<DataTransaksi>) :
    RecyclerView.Adapter<MutasiAdapter.MenuViewHolder>() {

    interface OnMenuItemClickListener {
        fun onItemClick(data: DataTransaksi)
    }

    private var itemClickListener: OnMenuItemClickListener? = null

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        itemClickListener = listener
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val cardMutasi = itemView.findViewById<CardView>(R.id.cardMutasi)
        val tgl = itemView.findViewById<TextView>(R.id.tgl)
        val nominal = itemView.findViewById<TextView>(R.id.nominal)
        val jenisTra = itemView.findViewById<TextView>(R.id.jenisTrans)
        val keterangan = itemView.findViewById<TextView>(R.id.keterangan)
        val layanan = itemView.findViewById<TextView>(R.id.layanan)

        init {
            cardMutasi.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val data = menuList[position]
                itemClickListener?.onItemClick(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_mutasi,
            parent, false
        )

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]

        if (currentItem.jenisTran == "DB"){
            holder.nominal.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.jenisTra.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            val nominalRupiah = currentItem.jumlah.let { Utils.formatToRupiah(it.toLong()) }
            holder.nominal.text = nominalRupiah
            holder.jenisTra.text = currentItem.jenisTran
        }else{
            holder.nominal.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue2))
            holder.jenisTra.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue2))
            val nominalRupiah = currentItem.jumlah.let { Utils.formatToRupiah(it.toLong()) }
            holder.nominal.text = nominalRupiah
            holder.jenisTra.text = currentItem.jenisTran
        }

        holder.layanan.text = currentItem.layanan
        holder.tgl.text = currentItem.tglTran
        holder.keterangan.text = currentItem.keterangan

    }

    override fun getItemCount() = menuList.size

    fun setData(newList: List<DataTransaksi>) {
        val diffCallback = MutasiAdapter.MenuDiffCallback(menuList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        menuList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class MenuDiffCallback(
        private val oldList: List<DataTransaksi>,
        private val newList: List<DataTransaksi>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].idRekening == newList[newItemPosition].idRekening

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }


}