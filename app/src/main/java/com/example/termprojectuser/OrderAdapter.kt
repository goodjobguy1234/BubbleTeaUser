package com.example.termprojectuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup

class OrderAdapter(val order:ArrayList<Order>, val callback: (Int, Int) -> Unit):RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private lateinit var mcontext: Context
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cancel_icon = itemView.findViewById<ImageView>(R.id.cancel_icon)
        val add_icon = itemView.findViewById<ImageView>(R.id.add_imageview)
        val minus_icon = itemView.findViewById<ImageView>(R.id.minus_imageview)
        val ordername = itemView.findViewById<TextView>(R.id.txt_ordername)
        val orderprice = itemView.findViewById<TextView>(R.id.txt_orderprice)
        val quantity = itemView.findViewById<TextView>(R.id.txt_quantity)

        fun bind(position: Int){
            quantity.text = order[position].quantity.toString()
            orderprice.text = order[position].item.price.toString()
            ordername.text = order[position].item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(mcontext).inflate(R.layout.orderlayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(position)
            cancel_icon.setOnClickListener {
                callback(position, 1)
            }
            minus_icon.setOnClickListener {
                callback(position, 3)
            }
            add_icon.setOnClickListener {
                if (order[position].item.checkRemain()){
                    callback(position, 2)
                }else{
                    Toast.makeText(mcontext, "This Menu Sold Out", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return order.size
    }


}