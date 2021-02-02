package com.example.termprojectuser

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
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
            if (menu[position].remainder == 2){
                image.setImageResource(menu[position].imageId)
                unavaliable(image)
            }else{
                image.setImageResource(menu[position].imageId)
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
                    Log.d("amount", menu[position].remainder.toString())
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
//        val matrix = ColorMatrix()
//        matrix.setSaturation(0f)
//        imageview.colorFilter = ColorMatrixColorFilter(matrix)
        val cm = ColorMatrix()
        val paint = Paint()
        cm.set(
                floatArrayOf(
                        0.33f, 0.33f, 0.33f, 0f, 0f,
                        0.33f, 0.33f, 0.33f, 0f, 0f,
                        0.33f, 0.33f, 0.33f, 0f, 0f,
                        0f, 0f, 0f, 1f, 0f
                )
        )
        imageview.colorFilter = ColorMatrixColorFilter(cm)
        
    }
}