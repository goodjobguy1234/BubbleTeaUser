package com.example.termprojectuser

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class MenuAdapter(val menu: ArrayList<Menu>, val callback: (Menu) -> Unit): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private lateinit var mcontext:Context
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.menuImage)
        val itemname = itemView.findViewById<TextView>(R.id.txt_name)
        val itemprice = itemView.findViewById<TextView>(R.id.txt_price)
        val add_btn = itemView.findViewById<ImageButton>(R.id.imageButton_add)

        fun bind(position: Int){
            itemname.text = menu[position].name
            itemprice.text = menu[position].price.toString()
            image.setImageResource(menu[position].imageId)
            if (!menu[position].checkRemain()){
                unavaliable(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(mcontext).inflate(R.layout.menulayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(position)
            add_btn.setOnClickListener {
                if (menu[position].checkRemain()){
                    callback(menu[position])
                }else{
                    Toast.makeText(mcontext, "This Menu Sold Out", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    fun unavaliable(imageview:ImageView){
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        imageview.colorFilter = ColorMatrixColorFilter(matrix)
    }
}