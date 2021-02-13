package com.example.termprojectuser

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class MenuAdapter(options: FirebaseRecyclerOptions<Menu>, val callback: (Menu?) -> Unit): FirebaseRecyclerAdapter<Menu,MenuAdapter.ViewHolder>(options) {
    private lateinit var mcontext:Context
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.menuImage)
        val itemname = itemView.findViewById<TextView>(R.id.txt_name)
        val itemprice = itemView.findViewById<TextView>(R.id.txt_price)
        val add_btn = itemView.findViewById<ImageButton>(R.id.imageButton_add)

        fun bind(model: Menu){
            itemname.text = model.name
            itemprice.text = model.price.toString()
            Glide.with(mcontext).load(model.imageUrl).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(mcontext).inflate(R.layout.menulayout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menu) {
        holder.apply {
            bind(model)
            add_btn.setOnClickListener {
                if (model.checkRemain()){
                    Log.d("amount", model.remain.toString())
                    callback(model)
                }else{
                    callback(null)
                }
            }
        }
    }
}