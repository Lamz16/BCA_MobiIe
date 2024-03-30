package com.bank.bcamobiie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.datadummy.MenuConfiguration

class MenuAdapter(private val menuList: List<MenuConfiguration>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface OnMenuItemClickListener {
        fun onItemClick(menuId: Int)
    }

    private var itemClickListener: OnMenuItemClickListener? = null

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        itemClickListener = listener
    }


    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageButton = itemView.findViewById(R.id.Imgmenu)
        val textView: TextView = itemView.findViewById(R.id.textMenu)

        init {
            imageView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val menuId = menuList[position].id
                itemClickListener?.onItemClick(menuId)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.menu_item,
            parent, false
        )

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]
        val drawable = holder.imageView.context.getDrawable(currentItem.image)

        holder.imageView.background = drawable
        holder.textView.text = currentItem.desc
    }

    override fun getItemCount() = menuList.size
}
