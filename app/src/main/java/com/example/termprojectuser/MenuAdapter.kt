package com.example.termprojectuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(val menu: ArrayList<Menu>, val callback: (Menu) -> Unit): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val itemimage = itemView.findViewById<ImageView>(R.id.imageView)
        val itemname = itemView.findViewById<TextView>(R.id.txt_name)
        val itemprice = itemView.findViewById<TextView>(R.id.txt_price)
        val add_btn = itemView.findViewById<Button>(R.id.imageButton_add)

        fun bind(position: Int){
            itemname.text = menu[position].name
            itemprice.text = menu[position].price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menulayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(position)
            add_btn.setOnClickListener {
                callback(menu[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return menu.size
    }
}