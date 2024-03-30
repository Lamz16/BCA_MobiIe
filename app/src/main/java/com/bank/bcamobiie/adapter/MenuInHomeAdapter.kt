package com.bank.bcamobiie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.datadummy.MenuInHome

class MenuInHomeAdapter(private val menuList: List<MenuInHome>) :
    RecyclerView.Adapter<MenuInHomeAdapter.MyViewHolder>() {

    interface OnMenuClickListener {
        fun onItemClick(menuId: Int)
    }

    private var itemClickListener: OnMenuClickListener? = null

    fun setOnMenuItemClickListener(listener: OnMenuClickListener) {

        itemClickListener = listener

    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val btnMenu = itemView.findViewById<Toolbar>(R.id.OptionMenu)
        val titleMen = itemView.findViewById<TextView>(R.id.titleMenu)
        val topTitleMen = itemView.findViewById<Toolbar>(R.id.titleTopMenu)
        val teksOptionMen = itemView.findViewById<TextView>(R.id.teksOptionMenu)


        init {
            btnMenu.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val menuId = menuList[position].id
                itemClickListener?.onItemClick(menuId)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_menu_minfo,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = menuList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMenu = menuList[position]
        if (currentMenu.title.isEmpty()) {
            holder.topTitleMen.visibility = View.GONE
            holder.titleMen.visibility = View.GONE
        } else {
            holder.topTitleMen.visibility = View.VISIBLE
            holder.titleMen.visibility = View.VISIBLE
            holder.titleMen.text = currentMenu.title
        }

        holder.teksOptionMen.text = currentMenu.menuName
    }


}